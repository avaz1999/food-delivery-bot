package food.delivery.bot.service.base;

import food.delivery.backend.entity.BotUser;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.List;

/**
 * Created by Avaz Absamatov
 * Date: 12/2/2025
 */
public interface StateMessageService {
    List<BotApiMethod<?>> handleStartMessage(BotUser botUser, String text);

    List<BotApiMethod<?>> handleSettingPhoneNumber(BotUser botUser, Message message);

    List<BotApiMethod<?>> handleSettingLocation(BotUser botUser, Message message);

    List<BotApiMethod<?>> handleOrderType(BotUser botUser, Message message);

    List<BotApiMethod<?>> handleChooseLocation(BotUser botUser, Message message);

    List<BotApiMethod<?>> handleChooseName(BotUser botUser, Message message);
}
