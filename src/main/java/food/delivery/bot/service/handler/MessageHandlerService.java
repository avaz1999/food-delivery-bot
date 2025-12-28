package food.delivery.bot.service.handler;

import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.List;

/**
 * Created by Avaz Absamatov
 * Date: 11/30/2025
 */
public interface MessageHandlerService {
    List<PartialBotApiMethod<?>> messageHandler(Message message);
}
