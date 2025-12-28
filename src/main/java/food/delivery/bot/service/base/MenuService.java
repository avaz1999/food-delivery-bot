package food.delivery.bot.service.base;

import food.delivery.backend.entity.BotUser;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.List;

/**
 * Created by Avaz Absamatov
 * Date: 12/6/2025
 */
public interface MenuService {
    List<PartialBotApiMethod<?>> addOrder(BotUser botUser, CallbackQuery callbackQuery);

    List<PartialBotApiMethod<?>> aboutUs(BotUser botUser, CallbackQuery callbackQuery);

    List<PartialBotApiMethod<?>> myOrders(BotUser botUser, CallbackQuery callbackQuery);

    List<PartialBotApiMethod<?>> comment(BotUser botUser, CallbackQuery callbackQuery);

    List<PartialBotApiMethod<?>> settingMenu(BotUser botUser, CallbackQuery callbackQuery);

    //Setting menu
    List<PartialBotApiMethod<?>> settingChangeLangMenu(BotUser botUser, CallbackQuery callbackQuery);

    List<PartialBotApiMethod<?>> settingChangePhoneMenu(BotUser botUser, CallbackQuery callbackQuery);

    List<PartialBotApiMethod<?>> settingChangeAddressMenu(BotUser botUser, CallbackQuery callbackQuery);

    List<PartialBotApiMethod<?>> mainMenu(BotUser botUser, CallbackQuery callbackQuery);
}
