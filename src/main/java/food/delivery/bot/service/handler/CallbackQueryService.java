package food.delivery.bot.service.handler;

import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.List;

/**
 * Created by Avaz Absamatov
 * Date: 11/30/2025
 */

public interface CallbackQueryService {
    List<PartialBotApiMethod<?>> callbackHandler(CallbackQuery callbackQuery);
}
