package food.delivery.bot.service.message;

import food.delivery.backend.dto.request.BotUserDTO;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.message.Message;

/**
 * Created by Avaz Absamatov
 * Date: 12/2/2025
 */
public interface ClientMessageService {
    BotApiMethod<?> handleClientState(Message message, BotUserDTO botUser);
}
