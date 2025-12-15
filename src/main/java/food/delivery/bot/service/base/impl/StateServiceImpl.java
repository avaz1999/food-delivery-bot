package food.delivery.bot.service.base.impl;

import food.delivery.backend.entity.BotUser;
import food.delivery.backend.enums.Language;
import food.delivery.backend.enums.State;
import food.delivery.backend.service.BotUserService;
import food.delivery.bot.service.base.BaseService;
import food.delivery.bot.service.base.MenuService;
import food.delivery.bot.service.base.ReplyMarkupService;
import food.delivery.bot.service.base.StateService;
import food.delivery.bot.utils.BotCommands;
import food.delivery.bot.utils.BotMessages;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.location.Location;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Avaz Absamatov
 * Date: 12/2/2025
 */
@Service
@RequiredArgsConstructor
public class StateServiceImpl implements StateService {
    private final MenuService menuService;
    private final BaseService baseService;
    private final BotUserService botUserService;
    private final ReplyMarkupService replyMarkupService;

    @Override
    public List<BotApiMethod<?>> handleStartMessage(BotUser botUser, String text) {
        botUserService.changeState(botUser, State.STATE_CHOOSE_LANG);
        return baseService.mainMenuMessage(botUser);
    }

    @Override
    public List<BotApiMethod<?>> handleChooseLanguage(BotUser botUser, CallbackQuery callbackQuery) {
        List<BotApiMethod<?>> result = processLanguage(botUser, callbackQuery);
        botUserService.changeState(botUser, State.STATE_MAIN_MENU);
        return result;
    }

    @Override
    public List<BotApiMethod<?>> handleSettingChangeLang(BotUser botUser, CallbackQuery callbackQuery) {
        return processLanguage(botUser, callbackQuery);
    }

    @Override
    public List<BotApiMethod<?>> handleSettingPhoneNumber(BotUser botUser, Message message) {
        if (message.hasContact()) {
            return processContact(botUser, message.getContact(), message.getMessageId());
        } else if (message.hasText()) {
            return processPhoneNumberText(botUser, message);
        } else
            return List.of(baseService.sendText(botUser.getChatId(), BotMessages.INVALID_MESSAGE.getMessage(botUser.getLanguage()), null));
    }

    @Override
    public List<BotApiMethod<?>> handleSettingLocation(BotUser botUser, Message message) {
        if (message.hasLocation()) {
            return processLocation(botUser, message.getLocation());
        } else if (message.hasText()) {
            return processLocationText(botUser, message.getText());
        } else
            return List.of(baseService.sendText(botUser.getChatId(), BotMessages.INVALID_MESSAGE.getMessage(botUser.getLanguage()), null));
    }


    @Override
    public List<BotApiMethod<?>> handleMainMenu(BotUser botUser, CallbackQuery callbackQuery) {
        String data = callbackQuery.getData();
        return switch (data) {
            case "MENU_START_ORDER" -> menuService.addOrder(botUser, data);
            case "MENU_ABOUT_US" -> menuService.aboutUs(botUser, data);
            case "MENU_MY_ORDER" -> menuService.myOrders(botUser, data);
            case "MENU_COMMENT" -> menuService.comment(botUser, data);
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

    private List<BotApiMethod<?>> processContact(BotUser botUser, Contact contact, Integer messageId) {
        Long userId = contact.getUserId();
        Long chatId = botUser.getChatId();
        Language language = botUser.getLanguage();

        if (!Objects.equals(userId, chatId)) {
            return List.of(baseService.sendText(
                    chatId,
                    BotMessages.INVALID_PHONE_NUMBER.getMessage(language),
                    null
            ));
        }
        String phoneNumber = contact.getPhoneNumber().startsWith("+")
                ? contact.getPhoneNumber().substring(1)
                : contact.getPhoneNumber();

        return phoneNumberSendMessage(botUser, messageId, phoneNumber);
    }

    private List<BotApiMethod<?>> processPhoneNumberText(BotUser botUser, Message message) {
        String text = message.getText();
        if (Objects.equals(BotCommands.MAIN_MENU.getMessage(botUser.getLanguage()), text)) {
            List<BotApiMethod<?>> res = new ArrayList<>(baseService.mainMenuMessage(botUser));
            res.removeFirst();
            List<BotApiMethod<?>> result = new ArrayList<>();
            String msg = BotMessages.ADD_ORDER_QUESTION.getMessage(botUser.getLanguage());
            result.add(baseService.sendText(botUser.getChatId(), msg, replyMarkupService.removeReplyKeyboard()));
            result.addAll(res);
            botUserService.changeState(botUser, State.STATE_MAIN_MENU);
            return result;
        } else {
            String phoneNumber = extractAndValidatePhone(text);
            if (phoneNumber == null) {
                String phoneNumberMessage = BotMessages.BOT_SHARE_PHONE_NUMBER_INVALID.getMessage(botUser.getLanguage());
                return List.of(baseService.sendText(botUser.getChatId(), phoneNumberMessage, replyMarkupService.sharePhone(botUser)));
            }
            return phoneNumberSendMessage(botUser, message.getMessageId(), phoneNumber);
        }
    }

    @NotNull
    private List<BotApiMethod<?>> phoneNumberSendMessage(BotUser botUser, Integer messageId, String phoneNumber) {
        BotUser savedUser = botUserService.savePhoneNumber(botUser, phoneNumber);
        Long chatId = botUser.getChatId();
        Language language = botUser.getLanguage();
        List<BotApiMethod<?>> response = new ArrayList<>();
        response.add(baseService.deleteMessage(
                chatId,
                messageId
        ));
        response.add(baseService.sendText(
                chatId,
                BotMessages.SUCCESS_CHANGE_PHONE_NUMBER.getMessage(language),
                replyMarkupService.removeReplyKeyboard()
        ));
        String address = botUser.getAddress() != null ? botUser.getAddress() : "";
        String message = BotMessages.SETTING_MENU.getMessageWPar(language, language, savedUser.getPhone(), address);
        SendMessage sendMessage = baseService.sendText(
                chatId,
                message,
                replyMarkupService.menuSetting(savedUser)
        );
        response.add(sendMessage);
        return response;
    }

    private List<BotApiMethod<?>> processLocation(BotUser botUser, Location location) {
        return null;
    }

    private List<BotApiMethod<?>> processLocationText(BotUser botUser, String text) {
        return null;
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

    public static String extractAndValidatePhone(String text) {
        if (text == null || text.trim().isEmpty()) return null;

        String cleaned = text.replaceAll("[^\\d+]", "");

        // +998XXYYYYYYY or 998XXYYYYYYY
        if (cleaned.matches("(\\+?998)\\d{9}")) {
            return cleaned.startsWith("+") ? cleaned.substring(1) : cleaned;
        }

        return null;
    }
}
