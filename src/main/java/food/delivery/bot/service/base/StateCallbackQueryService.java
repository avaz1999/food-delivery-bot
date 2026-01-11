package food.delivery.bot.service.base;

import food.delivery.backend.entity.BotUser;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.List;

/**
 * Created by Avaz Absamatov
 * Date: 12/24/2025
 */
public interface StateCallbackQueryService {
    List<PartialBotApiMethod<?>> handleChooseLanguage(BotUser botUser, CallbackQuery callbackQuery);

    List<PartialBotApiMethod<?>> handleMainMenu(BotUser botUser, CallbackQuery callbackQuery);

    List<PartialBotApiMethod<?>> handleSettingMenu(BotUser botUser, CallbackQuery callbackQuery);

    List<PartialBotApiMethod<?>> handleSettingChangeLang(BotUser botUser, CallbackQuery callbackQuery);

    List<PartialBotApiMethod<?>> handleChooseOrderType(BotUser botUser, CallbackQuery callbackQuery);

    List<PartialBotApiMethod<?>> handleChooseItem(BotUser botUser, CallbackQuery callbackQuery);

    List<PartialBotApiMethod<?>> handleMyCart(BotUser botUser, CallbackQuery callbackQuery);
}
