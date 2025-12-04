package food.delivery.bot.handler;

import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.message.Message;

/**
 * Created by Avaz Absamatov
 * Date: 11/30/2025
 */
public interface MessageHandlerService {
    BotApiMethod<?> messageHandler(Message message);
}
