package food.delivery.bot.service.message;

import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.List;

/**
 * Created by Avaz Absamatov
 * Date: 12/2/2025
 */
public interface DeveloperMessageService {
    List<BotApiMethod<?>> handleDeveloperState(Message message);
}
