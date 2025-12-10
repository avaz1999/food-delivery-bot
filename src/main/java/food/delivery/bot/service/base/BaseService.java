package food.delivery.bot.service.base;

import food.delivery.backend.dto.request.BotUserDTO;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.util.List;

/**
 * Created by Avaz Absamatov
 * Date: 12/2/2025
 */
public interface BaseService {
    SendMessage sendText(Long chatId, String text, ReplyKeyboard replyKeyboard);

    DeleteMessage deleteMessage(Long chatId, Integer messageId);

    EditMessageText editMessageText(Long chatId, String text, Integer messageId, InlineKeyboardMarkup replyKeyboard);

    List<BotApiMethod<?>> mainMenuMessage(BotUserDTO botUser);

    BotApiMethod<?> processSettingLanguage(BotUserDTO botUserDTO, Integer messageId, BaseService baseService, ReplyMarkupService replyMarkupService);
}
