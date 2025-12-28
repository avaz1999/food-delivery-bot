package food.delivery.bot.service.callback.impl;

import food.delivery.bot.service.callback.DeveloperCallbackQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.List;

/**
 * Created by Avaz Absamatov
 * Date: 12/6/2025
 */
@Service
@RequiredArgsConstructor
public class DeveloperCallbackQueryServiceImpl implements DeveloperCallbackQueryService {
    @Override
    public List<PartialBotApiMethod<?>> handleDeveloperState(CallbackQuery callbackQuery) {
        return List.of();
    }
}
