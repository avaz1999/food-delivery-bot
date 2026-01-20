package food.delivery.bot.service.message;

import food.delivery.backend.entity.BotUser;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.List;

/**
 * Created by Avaz Absamatov
 * Date: 12/2/2025
 */
public interface KitchenManagerMessageService {
    List<PartialBotApiMethod<?>> handleKitchenManagerState(Message message);

    SendMessage orderMessage(BotUser botUser);

}
