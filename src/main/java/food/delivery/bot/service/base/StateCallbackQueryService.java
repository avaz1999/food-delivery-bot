package food.delivery.bot.service.base;

import food.delivery.backend.entity.BotUser;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.List;

/**
 * Created by Avaz Absamatov
 * Date: 12/24/2025
 */
public interface StateCallbackQueryService {
    List<BotApiMethod<?>> handleChooseLanguage(BotUser botUser, CallbackQuery callbackQuery);

    List<BotApiMethod<?>> handleMainMenu(BotUser botUser, CallbackQuery callbackQuery);

    List<BotApiMethod<?>> handleSettingMenu(BotUser botUser, CallbackQuery callbackQuery);

    List<BotApiMethod<?>> handleSettingChangeLang(BotUser botUser, CallbackQuery callbackQuery);

    List<BotApiMethod<?>> handleChooseOrderType(BotUser botUser, CallbackQuery callbackQuery);
}
