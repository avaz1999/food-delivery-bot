package food.delivery.bot.service.message.impl;

import food.delivery.bot.service.message.DeveloperMessageService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.List;

/**
 * Created by Avaz Absamatov
 * Date: 12/2/2025
 */
@Service
public class DeveloperMessageServiceImpl implements DeveloperMessageService {
    @Override
    public List<BotApiMethod<?>> handleDeveloperState(Message message) {
        return null;
    }
}
