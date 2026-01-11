package food.delivery.bot.service.base.impl;

import food.delivery.backend.entity.BotUser;
import food.delivery.bot.service.base.BaseService;
import food.delivery.bot.service.base.ReplyMarkupService;
import food.delivery.bot.utils.BotMessages;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import static food.delivery.bot.utils.BotMessages.ADD_ORDER;
import static food.delivery.bot.utils.BotMessages.ADD_ORDER_QUESTION;

/**
 * Created by Avaz Absamatov
 * Date: 12/2/2025
 */
@Service
@RequiredArgsConstructor
public class BaseServiceImpl implements BaseService {
    private final ReplyMarkupService replyMarkupService;

    @Override
    public SendMessage sendMessage(Long chatId, String text, ReplyKeyboard replyKeyboard) {
        SendMessage sendMessage = SendMessage.builder()
                .parseMode("HTML")
                .chatId(chatId.toString())
                .text(text)
                .build();
        if (replyKeyboard != null) sendMessage.setReplyMarkup(replyKeyboard);
        return sendMessage;
    }

    @Override
    public DeleteMessage deleteMessage(Long chatId, Integer messageId) {
        return DeleteMessage.builder()
                .chatId(chatId.toString())
                .messageId(messageId)
                .build();
    }

    @Override
    public SendPhoto sendPhoto(Long chatId, String sendText, byte[] itemImage) {
        InputStream imageStream = new ByteArrayInputStream(itemImage);
        return SendPhoto.builder()
                .chatId(chatId.toString())
                .caption(sendText)
                .parseMode("HTML")
                .photo(new InputFile(imageStream, "item.jpg"))
                .build();
    }

    @Override
    public EditMessageText editMessageText(Long chatId, String text, Integer messageId, InlineKeyboardMarkup replyKeyboard) {
        return EditMessageText.builder()
                .chatId(chatId.toString())
                .messageId(messageId)
                .text(text)
                .parseMode("HTML")
                .replyMarkup(replyKeyboard)
                .build();
    }

    @Override
    public EditMessageCaption editMessageCaption(Long chatId, String caption, Integer messageId, InlineKeyboardMarkup replyKeyboard) {
        return EditMessageCaption.builder()
                .chatId(chatId)
                .messageId(messageId)
                .caption(caption)
                .parseMode("HTML")
                .replyMarkup(replyKeyboard)
                .build();
    }

    @Override
    @NotNull
    public List<PartialBotApiMethod<?>> mainMenuMessage(BotUser botUser) {
        String addOrderQuestion = ADD_ORDER_QUESTION.getMessage(botUser.getLanguage());
        String addOrder = ADD_ORDER.getMessage(botUser.getLanguage());
        return List.of(
                sendMessage(botUser.getChatId(), addOrderQuestion, null),
                sendMessage(botUser.getChatId(), addOrder, replyMarkupService.mainMenu(botUser))
        );
    }

    @Override
    public BotApiMethod<?> processSettingLanguage(BotUser botUser, Integer messageId, BaseService baseService, ReplyMarkupService replyMarkupService) {
        String language = botUser.getLanguage().name();
        String phone = botUser.getPhone() == null ? "" : botUser.getPhone();
        String address = botUser.getAddress() == null ? "" : botUser.getAddress();
        String message = BotMessages.SETTING_MENU.getMessageWPar(botUser.getLanguage(), language, phone, address);
        return baseService.editMessageText(
                botUser.getChatId(), message,
                messageId, replyMarkupService.menuSetting(botUser));
    }

    @Override
    public List<PartialBotApiMethod<?>> deleteAndSendMessageSender(BotUser botUser, Integer messageId, String message, ReplyKeyboard reply) {
        DeleteMessage deleteMessage = deleteMessage(botUser.getChatId(), messageId);
        SendMessage sendMessage = sendMessage(botUser.getChatId(),
                message, reply);
        return List.of(deleteMessage, sendMessage);
    }

}
