package food.delivery.bot.service.base;

import food.delivery.backend.entity.BotUser;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.List;

/**
 * Created by Avaz Absamatov
 * Date: 12/2/2025
 */
public interface StateMessageService {
    List<PartialBotApiMethod<?>> handleStartMessage(BotUser botUser, String text);

    List<PartialBotApiMethod<?>> handleSettingPhoneNumber(BotUser botUser, Message message);

    List<PartialBotApiMethod<?>> handleSettingLocation(BotUser botUser, Message message);

    List<PartialBotApiMethod<?>> handleOrderType(BotUser botUser, Message message);

    List<PartialBotApiMethod<?>> handleChooseLocation(BotUser botUser, Message message);

    List<PartialBotApiMethod<?>> handleChooseName(BotUser botUser, Message message);

    List<PartialBotApiMethod<?>> handleOrder(BotUser botUser, Message message);

    List<PartialBotApiMethod<?>> handleChoosePaymentType(BotUser botUser, Message message);
}
