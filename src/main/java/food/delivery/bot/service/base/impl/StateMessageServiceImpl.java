package food.delivery.bot.service.base.impl;

import food.delivery.backend.entity.BotUser;
import food.delivery.backend.enums.Language;
import food.delivery.backend.enums.State;
import food.delivery.backend.model.dto.CartDTO;
import food.delivery.backend.service.BotUserService;
import food.delivery.backend.service.CartService;
import food.delivery.backend.service.LocationService;
import food.delivery.bot.service.base.BaseService;
import food.delivery.bot.service.base.ReplyMarkupService;
import food.delivery.bot.service.base.StateMessageService;
import food.delivery.bot.service.base.TemplateBuilder;
import food.delivery.bot.utils.BotCommands;
import food.delivery.bot.utils.BotMessages;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
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
public class StateMessageServiceImpl implements StateMessageService {
    private final BaseService baseService;
    private final BotUserService botUserService;
    private final ReplyMarkupService replyMarkupService;
    private final LocationService locationService;
    private final CartService cartService;
    private final TemplateBuilder templateBuilder;

    @Override
    public List<PartialBotApiMethod<?>> handleStartMessage(BotUser botUser, String text) {
        botUserService.changeState(botUser, State.STATE_CHOOSE_LANG.name());
        return baseService.mainMenuMessage(botUser);
    }

    @Override
    public List<PartialBotApiMethod<?>> handleSettingPhoneNumber(BotUser botUser, Message message) {
        if (message.hasContact()) {
            return processContact(botUser, message.getContact(), message.getMessageId());
        } else if (message.hasText()) {
            return processPhoneNumberText(botUser, message);
        } else
            return List.of(baseService.sendMessage(botUser.getChatId(), BotMessages.INVALID_MESSAGE.getMessage(botUser.getLanguage()), null));
    }

    @Override
    public List<PartialBotApiMethod<?>> handleSettingLocation(BotUser botUser, Message message) {
        if (message.hasLocation()) {
            return processLocation(botUser, message.getLocation());
        } else if (message.hasText()) {
            return processLocationText(botUser, message);
        } else
            return List.of(baseService.deleteMessage(botUser.getChatId(), message.getMessageId()));
    }

    @Override
    public List<PartialBotApiMethod<?>> handleOrderType(BotUser botUser, Message message) {
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
    public List<PartialBotApiMethod<?>> handleChooseLocation(BotUser botUser, Message message) {
        if (message.hasLocation()) {
            return processLocation(botUser, message.getLocation());
        }
        String text = message.getText();

        if (Objects.equals(BotCommands.CHECK_YES.getMessage(botUser.getLanguage()), text)) {
            String successMessage = BotMessages.SUCCESS_ADD_ORDER.getMessage(botUser.getLanguage());
            ReplyKeyboard removeReply = replyMarkupService.removeReplyKeyboard();
            String sendText = BotMessages.CHOOSE_CATEGORY_ITEM.getMessage(botUser.getLanguage());
            ReplyKeyboard replyKeyboard = replyMarkupService.itemCategory(botUser, null, false);
            botUserService.changeState(botUser, State.CHOOSE_ITEM_CATEGORY.name());
            return List.of(baseService.sendMessage(botUser.getChatId(), successMessage, removeReply),
                    baseService.sendMessage(botUser.getChatId(), sendText, replyKeyboard));
        } else if (Objects.equals(BotCommands.CHECK_NO.getMessage(botUser.getLanguage()), text)) {
            String sendText = BotMessages.SEND_LOCATION_OR_CHOOSE_ADDRESS.getMessage(botUser.getLanguage());
            ReplyKeyboard replyKeyboard = replyMarkupService.sendLocation(botUser);
            return List.of(baseService.sendMessage(botUser.getChatId(), sendText, replyKeyboard));
        }
        if (!text.startsWith("✅ ")) {
            if (Objects.equals(text, BotCommands.BACK.getMessage(botUser.getLanguage()))) {
                String orderTypeText = BotMessages.CHOOSE_ORDER_TYPE.getMessage(botUser.getLanguage());
                ReplyKeyboard orderType = replyMarkupService.orderType(botUser);
                botUserService.changeState(botUser, State.STATE_CHOOSE_ORDER_TYPE.name());
                return List.of(baseService.sendMessage(botUser.getChatId(), orderTypeText, orderType));
            }
            String sendText = BotMessages.SEND_LOCATION_OR_CHOOSE_ADDRESS.getMessage(botUser.getLanguage());
            ReplyKeyboard replyKeyboard = replyMarkupService.sendLocationOrChooseAddress(botUser);
            SendMessage sendMessage = baseService.sendMessage(botUser.getChatId(), sendText, replyKeyboard);
            DeleteMessage deleteMessage = baseService.deleteMessage(botUser.getChatId(), message.getMessageId());
            return List.of(deleteMessage, sendMessage);
        } else {
            String deleteMessageText = BotMessages.SUCCESS_ADD_ORDER.getMessage(botUser.getLanguage());
            String sendText = BotMessages.ADD_ORDER.getMessage(botUser.getLanguage());
            ReplyKeyboard replyKeyboard = replyMarkupService.itemCategory(botUser, null, false);
            SendMessage deleteMessage = baseService.sendMessage(botUser.getChatId(), deleteMessageText, replyMarkupService.removeReplyKeyboard());
            SendMessage sendMessage = baseService.sendMessage(botUser.getChatId(), sendText, replyKeyboard);
            botUserService.changeState(botUser, State.CHOOSE_ITEM_CATEGORY.name());
            return List.of(deleteMessage, sendMessage);
        }
    }

    @Override
    public List<PartialBotApiMethod<?>> handleChooseName(BotUser botUser, Message message) {
        if (!message.hasText()) {
            DeleteMessage deleteMessage = baseService.deleteMessage(botUser.getChatId(), message.getMessageId());
            SendMessage sendMessage = baseService.sendMessage(botUser.getChatId(), message.getText(), replyMarkupService.enterName(botUser));
            return List.of(deleteMessage, sendMessage);
        }
        String text = message.getText();
        if (Objects.equals(BotCommands.CHECK_YES.getMessage(botUser.getLanguage()), text)) {
            String sendText = BotMessages.ADD_ORDER.getMessage(botUser.getLanguage());
            ReplyKeyboard replyKeyboard = replyMarkupService.itemCategory(botUser, null, false);
            SendMessage sendMessage = baseService.sendMessage(botUser.getChatId(), sendText, replyKeyboard);
            return List.of(sendMessage);
        }
        botUser.setFullName(text);
        String sendText = BotMessages.ACCEPT_NAME.getMessageWPar(botUser.getLanguage(), botUser.getFullName());
        ReplyKeyboard replyKeyboard = replyMarkupService.confirmData(botUser);
        SendMessage sendMessage = baseService.sendMessage(botUser.getChatId(), sendText, replyKeyboard);
        return List.of(sendMessage);
    }

    @Override
    public List<PartialBotApiMethod<?>> handleOrder(BotUser botUser, Message message) {
        if (!message.hasText()) {
            return deleteIncomingMessage(botUser, message);
        }

        if (message.hasContact()) {
            return handleContact(botUser, message);
        }

        if (isBackCommand(botUser, message)) {
            return handleBack(botUser, message);
        }

        String text = message.getText();
        String phone = extractAndValidatePhone(text);
        if (phone == null) return deleteIncomingMessage(botUser, message);
        botUserService.savePhoneNumber(botUser, phone, State.CHOOSE_PAYMENT_TYPE.name());
        String sendText = BotMessages.CHOOSE_PAYMENT_TYPE.getMessage(botUser.getLanguage());
        ReplyKeyboard replyKeyboard = replyMarkupService.paymentType(botUser);
        SendMessage sendMessage = baseService.sendMessage(botUser.getChatId(), sendText, replyKeyboard);

        return List.of(sendMessage);
    }
    @Override
    public List<PartialBotApiMethod<?>> handleChoosePaymentType(BotUser botUser, Message message) {
        if (!message.hasText()) {
            return deleteIncomingMessage(botUser, message);
        }
        String command = message.getText();
        if (Objects.equals(BotCommands.BACK.getMessage(botUser.getLanguage()), command)) {
            String text = BotMessages.BOT_SHARE_PHONE_NUMBER.getMessage(botUser.getLanguage());
            ReplyKeyboard replyKeyboard = replyMarkupService.sharePhoneForOrder(botUser);
            SendMessage sendMessage = baseService.sendMessage(botUser.getChatId(), text, replyKeyboard);
            botUserService.changeState(botUser, State.ORDER.name());
            return List.of(sendMessage);
        }
        return List.of();
    }

    private List<PartialBotApiMethod<?>> handleBack(BotUser botUser, Message message) {

        DeleteMessage deleteMessage =
                baseService.deleteMessage(botUser.getChatId(), message.getMessageId());

        CartDTO cart = cartService.getCartByUser(botUser);

        SendMessage cartMessage = baseService.sendMessage(
                botUser.getChatId(),
                buildCartText(botUser, cart),
                replyMarkupService.cartMenu(botUser, cart)
        );

        botUserService.changeState(botUser, State.MY_CART.name());

        return List.of(deleteMessage, cartMessage);
    }

    private List<PartialBotApiMethod<?>> handleContact(BotUser botUser, Message message) {

        Contact contact = message.getContact();

        if (!isOwnerContact(botUser, contact)) {
            return sendWrongContactMessage(botUser);
        }

        botUserService.savePhoneNumber(
                botUser,
                contact.getPhoneNumber(),
                State.CHOOSE_PAYMENT_TYPE.name()
        );

        return sendChoosePaymentType(botUser);
    }

    private boolean isBackCommand(BotUser botUser, Message message) {
        return Objects.equals(
                BotCommands.BACK.getMessage(botUser.getLanguage()),
                message.getText()
        );
    }

    private boolean isOwnerContact(BotUser botUser, Contact contact) {
        return Objects.equals(contact.getUserId(), botUser.getUserId());
    }

    private String buildCartText(BotUser botUser, CartDTO cart) {
        return BotMessages.CART_MESSAGE.getMessage(botUser.getLanguage())
                + templateBuilder.cartTemplate(botUser.getLanguage(), cart);
    }

    private List<PartialBotApiMethod<?>> sendWrongContactMessage(BotUser botUser) {

        SendMessage sendMessage = baseService.sendMessage(
                botUser.getChatId(),
                BotMessages.FAIL_CONTACT.getMessage(botUser.getLanguage()),
                replyMarkupService.sharePhoneForOrder(botUser)
        );

        return List.of(sendMessage);
    }

    private List<PartialBotApiMethod<?>> sendChoosePaymentType(BotUser botUser) {

        SendMessage sendMessage = baseService.sendMessage(
                botUser.getChatId(),
                BotMessages.CHOOSE_PAYMENT_TYPE.getMessage(botUser.getLanguage()),
                replyMarkupService.paymentType(botUser)
        );

        return List.of(sendMessage);
    }

    private List<PartialBotApiMethod<?>> deleteIncomingMessage(BotUser botUser, Message message) {
        return List.of(
                baseService.deleteMessage(botUser.getChatId(), message.getMessageId())
        );
    }

    private List<PartialBotApiMethod<?>> handleMainMenuCommand(BotUser botUser) {
        List<PartialBotApiMethod<?>> mainMenu = baseService.mainMenuMessage(botUser);

        List<PartialBotApiMethod<?>> res = new ArrayList<>(mainMenu);
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
        botUserService.changeState(botUser, State.STATE_MAIN_MENU.name());
        return res;
    }

    private List<PartialBotApiMethod<?>> processOrderPickup(BotUser botUser) {
        String sendText = BotMessages.ENTER_NAME.getMessage(botUser.getLanguage());
        ReplyKeyboard replyKeyboard = replyMarkupService.enterName(botUser);
        SendMessage sendMessage = baseService.sendMessage(botUser.getChatId(), sendText, replyKeyboard);
        return List.of(sendMessage);
    }

    private List<PartialBotApiMethod<?>> processOrderDelivery(BotUser botUser) {
        String sendText = BotMessages.SEND_LOCATION_OR_CHOOSE_ADDRESS.getMessage(botUser.getLanguage());
        ReplyKeyboard replyKeyboard = replyMarkupService.sendLocationOrChooseAddress(botUser);
        SendMessage sendMessage = baseService.sendMessage(botUser.getChatId(), sendText, replyKeyboard);
        botUserService.changeState(botUser, State.STATE_CHOOSE_LOCATION.name());
        return List.of(sendMessage);
    }


    private List<PartialBotApiMethod<?>> processContact(BotUser botUser, Contact contact, Integer messageId) {
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

    private List<PartialBotApiMethod<?>> processPhoneNumberText(BotUser botUser, Message message) {
        String text = message.getText();
        if (Objects.equals(BotCommands.MAIN_MENU.getMessage(botUser.getLanguage()), text)) {
            List<PartialBotApiMethod<?>> res = new ArrayList<>(baseService.mainMenuMessage(botUser));
            res.removeFirst();
            List<PartialBotApiMethod<?>> result = new ArrayList<>();
            String msg = BotMessages.ADD_ORDER_QUESTION.getMessage(botUser.getLanguage());
            result.add(baseService.sendMessage(botUser.getChatId(), msg, replyMarkupService.removeReplyKeyboard()));
            result.addAll(res);
            botUserService.changeState(botUser, State.STATE_MAIN_MENU.name());
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
    private List<PartialBotApiMethod<?>> phoneNumberSendMessage(BotUser botUser, Integer messageId, String phoneNumber) {
        BotUser savedUser = botUserService.savePhoneNumber(botUser, phoneNumber, State.STATE_SETTING_MENU.name());
        Long chatId = botUser.getChatId();
        Language language = botUser.getLanguage();
        List<PartialBotApiMethod<?>> response = new ArrayList<>();
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

    private List<PartialBotApiMethod<?>> processLocation(BotUser botUser, Location location) {
        Double latitude = location.getLatitude();
        Double longitude = location.getLongitude();
        String address = locationService.getAddressByLongitudeAndLatitude(latitude, longitude);
        botUser.setTemAddress(address);
        botUser = botUserService.saveTempAddress(botUser);

        //Save user location
        locationService.saveUserLocation(latitude, longitude, address);

        String message = BotMessages.USER_ADDRESS.getMessageWPar(botUser.getLanguage(), address);
        ReplyKeyboard replyKeyboard = replyMarkupService.confirmData(botUser);
        return List.of(baseService.sendMessage(botUser.getChatId(), message, replyKeyboard));
    }

    private List<PartialBotApiMethod<?>> processLocationText(BotUser botUser, Message message) {
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

    private List<PartialBotApiMethod<?>> handleTextAddress(BotUser botUser, String text) {
        botUser.setTemAddress(text);
        botUser = botUserService.saveTempAddress(botUser);
        String sendAddress = BotMessages.USER_ADDRESS.getMessageWPar(botUser.getLanguage(), botUser.getTemAddress());
        ReplyKeyboard replyKeyboard = replyMarkupService.confirmData(botUser);
        return List.of(baseService.sendMessage(botUser.getChatId(), sendAddress, replyKeyboard));
    }

    private List<PartialBotApiMethod<?>> handleCheckAddressYes(BotUser botUser) {
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

    private List<PartialBotApiMethod<?>> handleMainMenu(BotUser botUser) {
        List<PartialBotApiMethod<?>> res = baseService.mainMenuMessage(botUser);
        List<PartialBotApiMethod<?>> result = new ArrayList<>(res);
        String message = BotMessages.ADD_ORDER_QUESTION.getMessage(botUser.getLanguage());
        SendMessage sendMessage = baseService.sendMessage(botUser.getChatId(), message, replyMarkupService.removeReplyKeyboard());
        result.removeFirst();
        result.addFirst(sendMessage);
        botUserService.changeState(botUser, State.STATE_MAIN_MENU.name());
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
