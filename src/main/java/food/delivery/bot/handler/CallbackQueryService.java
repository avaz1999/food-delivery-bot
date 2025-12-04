package food.delivery.bot.handler;

import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

/**
 * Created by Avaz Absamatov
 * Date: 11/30/2025
 */

public interface CallbackQueryService {
    BotApiMethod<?> callbackHandler(CallbackQuery callbackQuery);
}
