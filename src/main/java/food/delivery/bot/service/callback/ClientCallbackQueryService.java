package food.delivery.bot.service.callback;

import food.delivery.backend.entity.BotUser;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.List;

/**
 * Created by Avaz Absamatov
 * Date: 12/6/2025
 */
public interface ClientCallbackQueryService {
    List<PartialBotApiMethod<?>> handleClientState(CallbackQuery callbackQuery, BotUser botUser);

}
