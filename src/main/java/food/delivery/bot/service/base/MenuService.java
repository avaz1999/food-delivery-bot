package food.delivery.bot.service.base;

import food.delivery.backend.dto.request.BotUserDTO;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.List;

/**
 * Created by Avaz Absamatov
 * Date: 12/6/2025
 */
public interface MenuService {
    List<BotApiMethod<?>> addOrder(BotUserDTO botUser, String data);
    List<BotApiMethod<?>> aboutUs(BotUserDTO botUser, String data);
    List<BotApiMethod<?>> myOrders(BotUserDTO botUser, String data);
    List<BotApiMethod<?>> comment(BotUserDTO botUser, String data);
    List<BotApiMethod<?>> setting(BotUserDTO botUser, CallbackQuery callbackQuery);

    //Setting menu
    List<BotApiMethod<?>> settingChangeLangMenu(BotUserDTO botUser, CallbackQuery callbackQuery);
    List<BotApiMethod<?>> settingChangeAddressMenu(BotUserDTO botUser, CallbackQuery callbackQuery);
    List<BotApiMethod<?>> settingChangePhoneMenu(BotUserDTO botUser, CallbackQuery callbackQuery);
    List<BotApiMethod<?>> mainMenu(BotUserDTO botUser, CallbackQuery callbackQuery);
}
