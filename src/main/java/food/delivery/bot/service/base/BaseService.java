package food.delivery.bot.service.base;

import food.delivery.backend.entity.BotUser;
import food.delivery.backend.model.dto.ItemDTO;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.util.List;

/**
 * Created by Avaz Absamatov
 * Date: 12/2/2025
 */
public interface BaseService {
    SendMessage sendMessage(Long chatId, String text, ReplyKeyboard replyKeyboard);

    DeleteMessage deleteMessage(Long chatId, Integer messageId);

    EditMessageText editMessageText(Long chatId, String text, Integer messageId, InlineKeyboardMarkup replyKeyboard);

    EditMessageCaption editMessageCaption(Long chatId, String caption, Integer messageId, InlineKeyboardMarkup replyKeyboard);

    List<PartialBotApiMethod<?>> mainMenuMessage(BotUser botUser);

    PartialBotApiMethod<?> processSettingLanguage(BotUser botUser, Integer messageId, BaseService baseService, ReplyMarkupService replyMarkupService);

    List<PartialBotApiMethod<?>> deleteAndSendMessageSender(BotUser botUser, Integer messageId, String message, ReplyKeyboard reply);


    SendPhoto sendPhoto(Long chatId, String sendText, ItemDTO item);
}
