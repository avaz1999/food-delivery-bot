package food.delivery.bot.service.callback;

import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.List;

/**
 * Created by Avaz Absamatov
 * Date: 12/6/2025
 */
public interface KitchenManagerCallbackQueryService {
    List<BotApiMethod<?>> handleKitchenManagerState(CallbackQuery callbackQuery);

}
