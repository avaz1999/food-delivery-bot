package food.delivery.bot.service.base;

import food.delivery.backend.dto.request.BotUserDTO;
import food.delivery.backend.entity.BotUser;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.List;

/**
 * Created by Avaz Absamatov
 * Date: 12/6/2025
 */
public interface MenuService {
    List<BotApiMethod<?>> addOrder(BotUser botUser, String data);
    List<BotApiMethod<?>> aboutUs(BotUser botUser, String data);
    List<BotApiMethod<?>> myOrders(BotUser botUser, String data);
    List<BotApiMethod<?>> comment(BotUser botUser, String data);
    List<BotApiMethod<?>> setting(BotUser botUser, CallbackQuery callbackQuery);

    //Setting menu
    List<BotApiMethod<?>> settingChangeLangMenu(BotUser botUser, CallbackQuery callbackQuery);
    List<BotApiMethod<?>> settingChangePhoneMenu(BotUser botUser, CallbackQuery callbackQuery);
    List<BotApiMethod<?>> settingChangeAddressMenu(BotUser botUser, CallbackQuery callbackQuery);
    List<BotApiMethod<?>> mainMenu(BotUser botUser, CallbackQuery callbackQuery);
}
