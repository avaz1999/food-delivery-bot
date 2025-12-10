package food.delivery.bot.service.base.impl;

import food.delivery.backend.dto.request.BotUserDTO;
import food.delivery.bot.service.base.BaseService;
import food.delivery.bot.service.base.ReplyMarkupService;
import food.delivery.bot.utils.BotMessages;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

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
    public SendMessage sendText(Long chatId, String text, ReplyKeyboard replyKeyboard) {
        SendMessage sendMessage = SendMessage.builder()
                .parseMode(ParseMode.MARKDOWN)
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
    public EditMessageText editMessageText(Long chatId, String text, Integer messageId, InlineKeyboardMarkup replyKeyboard) {
        return EditMessageText.builder()
                .chatId(chatId.toString())
                .messageId(messageId)
                .text(text)
                .replyMarkup(replyKeyboard)
                .build();
    }

    @Override
    @NotNull
    public List<BotApiMethod<?>> mainMenuMessage(BotUserDTO botUser) {
        String addOrderQuestion = ADD_ORDER_QUESTION.getMessage(botUser.getLanguage());
        String addOrder = ADD_ORDER.getMessage(botUser.getLanguage());
        return List.of(
                sendText(botUser.getChatId(), addOrderQuestion, null),
                sendText(botUser.getChatId(), addOrder, replyMarkupService.mainMenu(botUser))
        );
    }

    @Override
    public  BotApiMethod<?> processSettingLanguage(BotUserDTO botUserDTO, Integer messageId, BaseService baseService, ReplyMarkupService replyMarkupService) {
        String language = botUserDTO.getLanguage().name();
        String phone = botUserDTO.getPhone() == null ? "" : botUserDTO.getPhone();
        String address = botUserDTO.getAddress() == null ? "" : botUserDTO.getAddress();
        String message = BotMessages.SETTING_MENU.getMessageWPar(botUserDTO.getLanguage(), language, phone, address);
        return baseService.editMessageText(
                botUserDTO.getChatId(), message,
                messageId, replyMarkupService.menuSetting(botUserDTO));
    }
}
