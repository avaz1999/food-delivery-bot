package food.delivery.bot.service.message.impl;

import food.delivery.bot.service.message.KitchenManagerMessageService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.message.Message;

/**
 * Created by Avaz Absamatov
 * Date: 12/2/2025
 */
@Service
public class KitchenManagerMessageServiceImpl implements KitchenManagerMessageService {
    @Override
    public BotApiMethod<?> handleKitchenManagerState(Message message) {
        return null;
    }
}
