package food.delivery.bot.service.base;

import food.delivery.backend.dto.request.BotUserDTO;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.List;

/**
 * Created by Avaz Absamatov
 * Date: 12/2/2025
 */
public interface StateService {
    List<BotApiMethod<?>> handleStartMessage(BotUserDTO botUser, String text);

    List<BotApiMethod<?>> handleChooseLanguage(BotUserDTO botUser, CallbackQuery callbackQuery);

    List<BotApiMethod<?>> handleMainMenu(BotUserDTO botUser, CallbackQuery callbackQuery);

    List<BotApiMethod<?>> handleSettingMenu(BotUserDTO botUser, CallbackQuery callbackQuery);

    List<BotApiMethod<?>> handleSettingChangeLang(BotUserDTO botUser, CallbackQuery callbackQuery);
}
