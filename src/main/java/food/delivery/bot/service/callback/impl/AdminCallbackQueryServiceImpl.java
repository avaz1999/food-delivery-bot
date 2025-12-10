package food.delivery.bot.service.callback.impl;

import food.delivery.bot.service.callback.AdminCallbackQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.List;

/**
 * Created by Avaz Absamatov
 * Date: 12/6/2025
 */
@Service
@RequiredArgsConstructor
public class AdminCallbackQueryServiceImpl implements AdminCallbackQueryService {
    @Override
    public List<BotApiMethod<?>> handleAdminState(CallbackQuery callbackQuery) {
        return List.of();
    }
}
