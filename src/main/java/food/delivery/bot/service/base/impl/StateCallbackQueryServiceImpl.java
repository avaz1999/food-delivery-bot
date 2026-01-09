package food.delivery.bot.service.base.impl;

import food.delivery.backend.entity.BotUser;
import food.delivery.backend.enums.Language;
import food.delivery.backend.enums.State;
import food.delivery.backend.model.dto.CartDTO;
import food.delivery.backend.model.dto.ItemDTO;
import food.delivery.backend.service.BotUserService;
import food.delivery.backend.service.CartItemService;
import food.delivery.backend.service.ItemService;
import food.delivery.bot.service.base.*;
import food.delivery.bot.utils.BotCommands;
import food.delivery.bot.utils.BotMessages;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Avaz Absamatov
 * Date: 12/24/2025
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StateCallbackQueryServiceImpl implements StateCallbackQueryService {
    private final MenuService menuService;
    private final BaseService baseService;
    private final BotUserService botUserService;
    private final ReplyMarkupService replyMarkupService;
    private final ItemService itemService;
    private final CartItemService cartItemService;
    private final TemplateBuilder templateBuilder;

    @Override
    public List<PartialBotApiMethod<?>> handleChooseLanguage(BotUser botUser, CallbackQuery callbackQuery) {
        List<PartialBotApiMethod<?>> result = processLanguage(botUser, callbackQuery);
        botUserService.changeState(botUser, State.STATE_MAIN_MENU.name());
        return result;
    }

    @Override
    public List<PartialBotApiMethod<?>> handleMainMenu(BotUser botUser, CallbackQuery callbackQuery) {
        String data = callbackQuery.getData();
        return switch (data) {
            case "MENU_START_ORDER" -> menuService.addOrder(botUser, callbackQuery);
            case "MENU_ABOUT_US" -> menuService.aboutUs(botUser, callbackQuery);
            case "MENU_MY_ORDER" -> menuService.myOrders(botUser, callbackQuery);
            case "MENU_COMMENT" -> menuService.comment(botUser, callbackQuery);
            case "MENU_SETTINGS" -> menuService.settingMenu(botUser, callbackQuery);
            default -> throw new IllegalStateException("Unexpected value: " + data);
        };
    }

    @Override
    public List<PartialBotApiMethod<?>> handleSettingMenu(BotUser botUser, CallbackQuery callbackQuery) {
        String data = callbackQuery.getData();
        return switch (data) {
            case "SETTINGS_MENU_CHANGE_LANG" -> menuService.settingChangeLangMenu(botUser, callbackQuery);
            case "SETTINGS_MENU_CHANGE_PHONE" -> menuService.settingChangePhoneMenu(botUser, callbackQuery);
            case "SETTINGS_MENU_CHANGE_ADDRESS" -> menuService.settingChangeAddressMenu(botUser, callbackQuery);
            case "MAIN_MENU" -> menuService.mainMenu(botUser, callbackQuery);
            default -> throw new IllegalStateException("Unexpected value: " + data);
        };
    }

    @Override
    public List<PartialBotApiMethod<?>> handleSettingChangeLang(BotUser botUser, CallbackQuery callbackQuery) {
        return processLanguage(botUser, callbackQuery);
    }

    @Override
    public List<PartialBotApiMethod<?>> handleChooseOrderType(BotUser botUser, CallbackQuery callbackQuery) {
        Integer messageId = callbackQuery.getMessage().getMessageId();
        String data = callbackQuery.getData();
        if (Objects.equals(data, BotCommands.MAIN_MENU.name())) {
            return buttonMainMenu(botUser, callbackQuery);
        }
        String[] split = data.split("#");
        Long categoryId = Long.valueOf(split[1]);
        if (Objects.equals(split[0], BotCommands.BACK.name())) {
            return buttonBack(botUser, categoryId, messageId);
        }
        ItemDTO item = itemService.getItemByCategoryId(categoryId, botUser.getLanguage());
        if (item != null) {
            return getOneItem(botUser, categoryId, item, callbackQuery.getMessage().getMessageId());
        }
        String editText = BotMessages.CHOOSE_CATEGORY_ITEM.getMessage(botUser.getLanguage());
        InlineKeyboardMarkup replyKeyboard = replyMarkupService.itemCategory(botUser, categoryId, true);
        EditMessageText editMessage = baseService.editMessageText(botUser.getChatId(), editText, messageId, replyKeyboard);
        return List.of(editMessage);
    }

    @Override
    public List<PartialBotApiMethod<?>> handleChooseItem(
            BotUser botUser,
            CallbackQuery callbackQuery
    ) {
        String[] split = callbackQuery.getData().split("#");
        String command = split[0];

        if (Objects.equals(BotCommands.PLUS.name(), command)) {
            return handlePlus(botUser, callbackQuery, split);
        }

        if (Objects.equals(BotCommands.MINUS.name(), command)) {
            return handleMinus(botUser, callbackQuery, split);
        }

        if (Objects.equals(BotCommands.CART.name(), command)) {
            return handleCart(botUser, callbackQuery, split);
        }
        if (Objects.equals(BotCommands.BACK.name(), command)) {
            return handleBack(botUser, callbackQuery, split);
        }

        return List.of();
    }

    private List<PartialBotApiMethod<?>> handleBack(BotUser botUser, CallbackQuery callback, String[] split) {
        Long categoryId = Long.valueOf(split[1]);
        Integer messageId = callback.getMessage().getMessageId();
        String editText = BotMessages.CHOOSE_CATEGORY_ITEM.getMessage(botUser.getLanguage());
        DeleteMessage deleteMessage = baseService.deleteMessage(botUser.getChatId(), messageId);
        InlineKeyboardMarkup replyKeyboard = replyMarkupService.itemCategory(botUser, categoryId, false);
        SendMessage sendMessage = baseService.sendMessage(botUser.getChatId(), editText, replyKeyboard);
        botUserService.changeState(botUser, State.CHOOSE_ITEM_CATEGORY.name());
        return List.of(deleteMessage, sendMessage);
    }

    private List<PartialBotApiMethod<?>> handleCart(BotUser botUser, CallbackQuery callback, String[] split) {
        Long categoryId = Long.valueOf(split[1]);
        int quantity = Integer.parseInt(split[2]);
        Long itemId = Long.valueOf(split[3]);
        CartDTO cartDTO = cartItemService.collectItem(itemId, quantity, botUser);
        String message = templateBuilder.cartTemplate(botUser.getLanguage(), cartDTO);
        InlineKeyboardMarkup markup = replyMarkupService.itemCategory(botUser, categoryId, false);
        DeleteMessage deleteMessage = baseService.deleteMessage(botUser.getChatId(), callback.getMessage().getMessageId());
        SendMessage sendMessage = baseService.sendMessage(botUser.getChatId(), message, markup);
        botUserService.changeState(botUser, State.CHOOSE_ITEM_CATEGORY.name());
        return List.of(deleteMessage, sendMessage);
    }

    private List<PartialBotApiMethod<?>> handlePlus(
            BotUser botUser, CallbackQuery callbackQuery, String[] split
    ) {
        Long categoryId = Long.valueOf(split[1]);
        int count = Integer.parseInt(split[2]) + 1;
        Long itemId = Long.valueOf(split[3]);
        Integer messageId = callbackQuery.getMessage().getMessageId();

        return updateItemMessage(botUser, categoryId, count, messageId, itemId);
    }

    private List<PartialBotApiMethod<?>> handleMinus(
            BotUser botUser,
            CallbackQuery callbackQuery,
            String[] split
    ) {
        int count = Integer.parseInt(split[2]);
        Long itemId = Long.valueOf(split[3]);
        if (count <= 1) {
            return List.of();
        }

        Long categoryId = Long.valueOf(split[1]);
        Integer messageId = callbackQuery.getMessage().getMessageId();

        return updateItemMessage(botUser, categoryId, count - 1, messageId, itemId);
    }

    private List<PartialBotApiMethod<?>> updateItemMessage(
            BotUser botUser,
            Long categoryId,
            int count,
            Integer messageId,
            Long itemId) {
        InlineKeyboardMarkup replyKeyboard =
                replyMarkupService.oneItemReply(botUser, categoryId, count, itemId);

        ItemDTO item =
                itemService.getItemByCategoryId(categoryId, botUser.getLanguage());

        BigDecimal price =
                (item.getDiscountPrice() != null &&
                        item.getDiscountPrice().compareTo(BigDecimal.ZERO) > 0)
                        ? item.getDiscountPrice()
                        : item.getPrice();

        String caption = BotMessages.CHOOSE_ITEM.getMessageWPar(
                botUser.getLanguage(),
                item.getName(),
                price,
                item.getDescription()
        );

        EditMessageCaption editMessageCaption =
                baseService.editMessageCaption(
                        botUser.getChatId(),
                        caption,
                        messageId,
                        replyKeyboard
                );

        return List.of(editMessageCaption);
    }

    private List<PartialBotApiMethod<?>> getOneItem(BotUser botUser, Long categoryId, ItemDTO item, Integer messageId) {
        BigDecimal price =
                (item.getDiscountPrice() != null && item.getDiscountPrice().compareTo(BigDecimal.ZERO) > 0)
                        ? item.getDiscountPrice()
                        : item.getPrice();

        String sendText = BotMessages.CHOOSE_ITEM.getMessageWPar(
                botUser.getLanguage(),
                item.getName(),
                price,
                item.getDescription());
        DeleteMessage deleteMessage = baseService.deleteMessage(botUser.getChatId(), messageId);
        SendPhoto sendPhoto = baseService.sendPhoto(botUser.getChatId(), sendText, item.getImage());

        ReplyKeyboard replyKeyboard = replyMarkupService.oneItemReply(botUser, categoryId, 1, item.getId());
        sendPhoto.setReplyMarkup(replyKeyboard);

        botUserService.changeState(botUser, State.CHOOSE_ITEM.name());
        return List.of(deleteMessage, sendPhoto);
    }

    private List<PartialBotApiMethod<?>> buttonBack(BotUser botUser, Long categoryId, Integer messageId) {
        String editText = BotMessages.CHOOSE_CATEGORY_ITEM.getMessage(botUser.getLanguage());
        InlineKeyboardMarkup markup = replyMarkupService.itemCategory(botUser, categoryId, false);
        EditMessageText editMessage = baseService.editMessageText(botUser.getChatId(), editText, messageId, markup);
        return List.of(editMessage);
    }

    private List<PartialBotApiMethod<?>> buttonMainMenu(BotUser botUser, CallbackQuery callback) {
        String sendText = BotMessages.ADD_ORDER.getMessage(botUser.getLanguage());
        InlineKeyboardMarkup markup = replyMarkupService.mainMenu(botUser);
        DeleteMessage deleteMessage = baseService.deleteMessage(botUser.getChatId(), callback.getMessage().getMessageId());
        SendMessage sendMessage = baseService.sendMessage(botUser.getChatId(), sendText, markup);
        botUserService.changeState(botUser, State.STATE_MAIN_MENU.name());
        return List.of(deleteMessage, sendMessage);
    }

    @NotNull
    private List<PartialBotApiMethod<?>> processLanguage(BotUser botUser, CallbackQuery callbackQuery) {
        String data = callbackQuery.getData();
        Integer messageId = callbackQuery.getMessage().getMessageId();
        Language language;
        List<PartialBotApiMethod<?>> result = new ArrayList<>();

        if (Objects.equals(data, BotCommands.BACK.name())) {
            PartialBotApiMethod<?> editMessage = baseService.processSettingLanguage(botUser, messageId, baseService, replyMarkupService);
            result.add(editMessage);
            botUserService.changeState(botUser, State.STATE_SETTING_MENU.name());
            return result;
        }
        if (Objects.equals(data, Language.UZ.name()))
            language = botUserService.saveLanguage(botUser, Language.UZ);

        else language = botUserService.saveLanguage(botUser, Language.RU);

        botUser.setLanguage(language);
        List<PartialBotApiMethod<?>> res = baseService.mainMenuMessage(botUser);
        result.addAll(res);
        result.removeFirst();
        botUserService.changeState(botUser, State.STATE_MAIN_MENU.name());
        DeleteMessage deleteMessage = baseService.deleteMessage(botUser.getChatId(), messageId);
        result.add(deleteMessage);
        return result;
    }
}
