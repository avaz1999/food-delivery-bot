package food.delivery.bot.service.base.impl;

import food.delivery.backend.entity.BotUser;
import food.delivery.backend.enums.Language;
import food.delivery.backend.enums.State;
import food.delivery.backend.service.BotUserService;
import food.delivery.backend.service.LocationService;
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
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

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
    private final LocationService locationService;

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
            return List.of(baseService.sendMessage(botUser.getChatId(), BotMessages.INVALID_MESSAGE.getMessage(botUser.getLanguage()), null));
    }

    @Override
    public List<BotApiMethod<?>> handleSettingLocation(BotUser botUser, Message message) {
        if (message.hasLocation()) {
            return processLocation(botUser, message.getLocation());
        } else if (message.hasText()) {
            return processLocationText(botUser, message);
        } else
            return List.of(baseService.deleteMessage(botUser.getChatId(), message.getMessageId()));
    }

    @Override
    public List<BotApiMethod<?>> handleOrderType(BotUser botUser, Message message) {
        if (!message.hasText()) {
            String sendText = BotMessages.CHOOSE_ORDER_TYPE.getMessage(botUser.getLanguage());
            ReplyKeyboard replyKeyboard = replyMarkupService.orderType(botUser);
            return baseService.deleteAndSendMessageSender(botUser, message.getMessageId(), sendText, replyKeyboard);
        }
        String text = message.getText();
        if (Objects.equals(BotCommands.ORDER_DELIVERY.getMessage(botUser.getLanguage()), text)) {
            return processOrderDelivery(botUser);
        }

        if (Objects.equals(BotCommands.ORDER_PICKUP.getMessage(botUser.getLanguage()), text)) {
            return processOrderPickup(botUser);
        }

        if (Objects.equals(BotCommands.MAIN_MENU.getMessage(botUser.getLanguage()), text)) {
            return handleMainMenuCommand(botUser);
        }
        return List.of(baseService.deleteMessage(botUser.getChatId(), message.getMessageId()));
    }

    @Override
    public List<BotApiMethod<?>> handleChooseLocation(BotUser botUser, Message message) {
        if (message.hasLocation()) {
            return processLocation(botUser, message.getLocation());
        }
        String text = message.getText();

        if (Objects.equals(BotCommands.CHECK_YES.getMessage(botUser.getLanguage()), text)) {
            String sendText = BotMessages.CHOOSE_CATEGORY_ITEM.getMessage(botUser.getLanguage());
            ReplyKeyboard replyKeyboard = replyMarkupService.itemCategory(botUser);
            botUserService.changeState(botUser, State.CHOOSE_ITEM_CATEGORY);
            return List.of(baseService.sendMessage(botUser.getChatId(), sendText, replyKeyboard));
        } else if (Objects.equals(BotCommands.CHECK_NO.getMessage(botUser.getLanguage()), text)) {

        }
        if (!text.startsWith("✅ ")) {
            String sendText = BotMessages.SEND_LOCATION_OR_CHOOSE_ADDRESS.getMessage(botUser.getLanguage());
            ReplyKeyboard replyKeyboard = replyMarkupService.sendLocationOrChooseAddress(botUser);
            SendMessage sendMessage = baseService.sendMessage(botUser.getChatId(), sendText, replyKeyboard);
            DeleteMessage deleteMessage = baseService.deleteMessage(botUser.getChatId(), message.getMessageId());
            return List.of(deleteMessage, sendMessage);
        }
        return List.of();
    }

    @Override
    public List<BotApiMethod<?>> handleChooseName(BotUser botUser, Message message) {
        if (!message.hasText()) {
            DeleteMessage deleteMessage = baseService.deleteMessage(botUser.getChatId(), message.getMessageId());
            SendMessage sendMessage = baseService.sendMessage(botUser.getChatId(), message.getText(), replyMarkupService.enterName(botUser));
            return List.of(deleteMessage, sendMessage);
        }
        String text = message.getText();
        if (Objects.equals(BotCommands.CHECK_YES.getMessage(botUser.getLanguage()), text)) {

        }
        botUser.setFullName(text);
        String sendText = BotMessages.ACCEPT_NAME.getMessageWPar(botUser.getLanguage(), botUser.getFullName());
        ReplyKeyboard replyKeyboard = replyMarkupService.confirmData(botUser);
        SendMessage sendMessage = baseService.sendMessage(botUser.getChatId(), sendText, replyKeyboard);
        return List.of(sendMessage);
    }

    private List<BotApiMethod<?>> handleMainMenuCommand(BotUser botUser) {
        List<BotApiMethod<?>> mainMenu = baseService.mainMenuMessage(botUser);

        List<BotApiMethod<?>> res = new ArrayList<>(mainMenu);
        if (!res.isEmpty()) {
            res.removeFirst();
        }

        String questionText = BotMessages.ADD_ORDER_QUESTION.getMessage(botUser.getLanguage());
        ReplyKeyboard removeKeyboard = replyMarkupService.removeReplyKeyboard();
        SendMessage questionMessage = baseService.sendMessage(
                botUser.getChatId(),
                questionText,
                removeKeyboard
        );
        res.addFirst(questionMessage);
        return res;
    }

    private List<BotApiMethod<?>> processOrderPickup(BotUser botUser) {
        String sendText = BotMessages.ENTER_NAME.getMessage(botUser.getLanguage());
        ReplyKeyboard replyKeyboard = replyMarkupService.enterName(botUser);
        SendMessage sendMessage = baseService.sendMessage(botUser.getChatId(), sendText, replyKeyboard);
        return List.of(sendMessage);
    }

    private List<BotApiMethod<?>> processOrderDelivery(BotUser botUser) {
        String sendText = BotMessages.SEND_LOCATION_OR_CHOOSE_ADDRESS.getMessage(botUser.getLanguage());
        ReplyKeyboard replyKeyboard = replyMarkupService.sendLocationOrChooseAddress(botUser);
        SendMessage sendMessage = baseService.sendMessage(botUser.getChatId(), sendText, replyKeyboard);
        botUserService.changeState(botUser, State.STATE_CHOOSE_LOCATION);
        return List.of(sendMessage);
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

    private List<BotApiMethod<?>> processContact(BotUser botUser, Contact contact, Integer messageId) {
        Long userId = contact.getUserId();
        Long chatId = botUser.getChatId();
        Language language = botUser.getLanguage();

        if (!Objects.equals(userId, chatId)) {
            return List.of(baseService.sendMessage(
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
            result.add(baseService.sendMessage(botUser.getChatId(), msg, replyMarkupService.removeReplyKeyboard()));
            result.addAll(res);
            botUserService.changeState(botUser, State.STATE_MAIN_MENU);
            return result;
        } else {
            String phoneNumber = extractAndValidatePhone(text);
            if (phoneNumber == null) {
                String phoneNumberMessage = BotMessages.BOT_SHARE_PHONE_NUMBER_INVALID.getMessage(botUser.getLanguage());
                return List.of(baseService.sendMessage(botUser.getChatId(), phoneNumberMessage, replyMarkupService.sharePhone(botUser)));
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
        response.add(baseService.sendMessage(
                chatId,
                BotMessages.SUCCESS_CHANGE_PHONE_NUMBER.getMessage(language),
                replyMarkupService.removeReplyKeyboard()
        ));
        String address = botUser.getAddress() != null ? botUser.getAddress() : "";
        String message = BotMessages.SETTING_MENU.getMessageWPar(language, language, savedUser.getPhone(), address);
        SendMessage sendMessage = baseService.sendMessage(
                chatId,
                message,
                replyMarkupService.menuSetting(savedUser)
        );
        response.add(sendMessage);
        return response;
    }

    private List<BotApiMethod<?>> processLocation(BotUser botUser, Location location) {
        Double latitude = location.getLatitude();
        Double longitude = location.getLongitude();
        String address = locationService.getAddressByLongitudeAndLatitude(latitude, longitude);
        botUser.setTemAddress(address);
        botUser = botUserService.saveTempAddress(botUser);

        String message = BotMessages.USER_ADDRESS.getMessageWPar(botUser.getLanguage(), address);
        ReplyKeyboard replyKeyboard = replyMarkupService.confirmData(botUser);
        return List.of(baseService.sendMessage(botUser.getChatId(), message, replyKeyboard));
    }

    private List<BotApiMethod<?>> processLocationText(BotUser botUser, Message message) {
        String text = message.getText();
        if (Objects.equals(BotCommands.MAIN_MENU.getMessage(botUser.getLanguage()), text)) {
            return handleMainMenu(botUser);
        } else if (Objects.equals(BotCommands.CHECK_YES.getMessage(botUser.getLanguage()), text)) {
            return handleCheckAddressYes(botUser);
        } else if (Objects.equals(BotCommands.CHECK_NO.getMessage(botUser.getLanguage()), text)) {
            return List.of(baseService.sendMessage(botUser.getChatId(), BotMessages.ADD_ADDRESS.getMessage(botUser.getLanguage()), replyMarkupService.sendLocation(botUser)));
        } else {
            return handleTextAddress(botUser, message.getText());
        }
    }

    private List<BotApiMethod<?>> handleTextAddress(BotUser botUser, String text) {
        botUser.setTemAddress(text);
        botUser = botUserService.saveTempAddress(botUser);
        String sendAddress = BotMessages.USER_ADDRESS.getMessageWPar(botUser.getLanguage(), botUser.getTemAddress());
        ReplyKeyboard replyKeyboard = replyMarkupService.confirmData(botUser);
        return List.of(baseService.sendMessage(botUser.getChatId(), sendAddress, replyKeyboard));
    }

    private List<BotApiMethod<?>> handleCheckAddressYes(BotUser botUser) {
        botUser = botUserService.saveAddress(botUser);
        String language = botUser.getLanguage().name();
        String phone = botUser.getPhone() == null ? "" : botUser.getPhone();
        String address = botUser.getAddress() == null ? "" : botUser.getAddress();
        String message = BotMessages.SETTING_MENU.getMessageWPar(botUser.getLanguage(), language, phone, address);
        String savedAddress = BotMessages.SAVED_ADDRESS.getMessage(botUser.getLanguage());
        SendMessage sendMessage = baseService.sendMessage(botUser.getChatId(), savedAddress, replyMarkupService.removeReplyKeyboard());
        SendMessage sendSettingMenu = baseService.sendMessage(botUser.getChatId(), message, replyMarkupService.menuSetting(botUser));
        return List.of(sendMessage, sendSettingMenu);
    }

    private List<BotApiMethod<?>> handleMainMenu(BotUser botUser) {
        List<BotApiMethod<?>> res = baseService.mainMenuMessage(botUser);
        List<BotApiMethod<?>> result = new ArrayList<>(res);
        String message = BotMessages.ADD_ORDER_QUESTION.getMessage(botUser.getLanguage());
        SendMessage sendMessage = baseService.sendMessage(botUser.getChatId(), message, replyMarkupService.removeReplyKeyboard());
        result.removeFirst();
        result.addFirst(sendMessage);
        botUserService.changeState(botUser, State.STATE_MAIN_MENU);
        return result;
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

        if (cleaned.matches("(\\+?998)\\d{9}")) {
            return cleaned.startsWith("+") ? cleaned.substring(1) : cleaned;
        }

        return null;
    }
}
