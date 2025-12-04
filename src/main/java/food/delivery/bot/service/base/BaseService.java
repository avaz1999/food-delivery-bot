package food.delivery.bot.service.base;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

/**
 * Created by Avaz Absamatov
 * Date: 12/2/2025
 */
public interface BaseService {
    SendMessage sendText(Long chatId, String text, ReplyKeyboard replyKeyboard);
}
