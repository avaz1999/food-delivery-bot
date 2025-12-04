package food.delivery.bot.handler.impl;

import food.delivery.bot.handler.CallbackQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

/**
 * Created by Avaz Absamatov
 * Date: 11/30/2025
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CallbackQueryServiceImpl implements CallbackQueryService {
    @Override
    public BotApiMethod<?> callbackHandler(CallbackQuery callbackQuery) {
        return null;
    }
}
