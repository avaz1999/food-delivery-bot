package food.delivery.bot.service.base.impl;

import food.delivery.backend.entity.BotUser;
import food.delivery.backend.enums.Language;
import food.delivery.backend.enums.State;
import food.delivery.backend.service.BotUserService;
import food.delivery.bot.service.base.BaseService;
import food.delivery.bot.service.base.MenuService;
import food.delivery.bot.service.base.ReplyMarkupService;
import food.delivery.bot.service.base.StateCallbackQueryService;
import food.delivery.bot.utils.BotCommands;
import food.delivery.bot.utils.BotMessages;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Avaz Absamatov
 * Date: 12/24/2025
 */
@Service
@RequiredArgsConstructor
public class StateCallbackQueryServiceImpl implements StateCallbackQueryService {
    private final MenuService menuService;
    private final BaseService baseService;
    private final BotUserService botUserService;
    private final ReplyMarkupService replyMarkupService;

    @Override
    public List<BotApiMethod<?>> handleChooseLanguage(BotUser botUser, CallbackQuery callbackQuery) {
        List<BotApiMethod<?>> result = processLanguage(botUser, callbackQuery);
        botUserService.changeState(botUser, State.STATE_MAIN_MENU);
        return result;
    }

    @Override
    public List<BotApiMethod<?>> handleMainMenu(BotUser botUser, CallbackQuery callbackQuery) {
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
    public List<BotApiMethod<?>> handleSettingMenu(BotUser botUser, CallbackQuery callbackQuery) {
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
    public List<BotApiMethod<?>> handleSettingChangeLang(BotUser botUser, CallbackQuery callbackQuery) {
        return processLanguage(botUser, callbackQuery);
    }

    @Override
    public List<BotApiMethod<?>> handleChooseOrderType(BotUser botUser, CallbackQuery callbackQuery) {
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
        String editText = BotMessages.CHOOSE_CATEGORY_ITEM.getMessage(botUser.getLanguage());
        InlineKeyboardMarkup replyKeyboard = replyMarkupService.itemCategory(botUser, categoryId, true);
        EditMessageText editMessage = baseService.editMessageText(botUser.getChatId(), editText, messageId, replyKeyboard);
        return List.of(editMessage);
    }

    private List<BotApiMethod<?>> buttonBack(BotUser botUser, Long categoryId, Integer messageId) {
        String editText = BotMessages.CHOOSE_CATEGORY_ITEM.getMessage(botUser.getLanguage());
        InlineKeyboardMarkup markup = replyMarkupService.itemCategory(botUser, categoryId, false);
        EditMessageText editMessage = baseService.editMessageText(botUser.getChatId(), editText, messageId, markup);
        return List.of(editMessage);
    }

    private List<BotApiMethod<?>> buttonMainMenu(BotUser botUser, CallbackQuery callback) {
        String sendText = BotMessages.ADD_ORDER.getMessage(botUser.getLanguage());
        InlineKeyboardMarkup markup = replyMarkupService.mainMenu(botUser);
        DeleteMessage deleteMessage = baseService.deleteMessage(botUser.getChatId(), callback.getMessage().getMessageId());
        SendMessage sendMessage = baseService.sendMessage(botUser.getChatId(), sendText, markup);
        botUserService.changeState(botUser, State.STATE_MAIN_MENU);
        return List.of(deleteMessage, sendMessage);
    }

    @NotNull
    private List<BotApiMethod<?>> processLanguage(BotUser botUser, CallbackQuery callbackQuery) {
        String data = callbackQuery.getData();
        Integer messageId = callbackQuery.getMessage().getMessageId();
        Language language;
        List<BotApiMethod<?>> result = new ArrayList<>();

        if (Objects.equals(data, BotCommands.BACK.name())) {
            BotApiMethod<?> editMessage = baseService.processSettingLanguage(botUser, messageId, baseService, replyMarkupService);
            result.add(editMessage);
            botUserService.changeState(botUser, State.STATE_SETTING_MENU);
            return result;
        }
        if (Objects.equals(data, Language.UZ.name()))
            language = botUserService.saveLanguage(botUser, Language.UZ);

        else language = botUserService.saveLanguage(botUser, Language.RU);

        botUser.setLanguage(language);
        List<BotApiMethod<?>> res = baseService.mainMenuMessage(botUser);
        result.addAll(res);
        result.removeFirst();
        botUserService.changeState(botUser, State.STATE_MAIN_MENU);
        DeleteMessage deleteMessage = baseService.deleteMessage(botUser.getChatId(), messageId);
        result.add(deleteMessage);
        return result;
    }
}
