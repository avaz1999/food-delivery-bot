package food.delivery.bot.service.base;

import food.delivery.backend.entity.BotUser;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

/**
 * Created by Avaz Absamatov
 * Date: 12/2/2025
 */
public interface ReplyMarkupService {
    InlineKeyboardMarkup mainMenu(BotUser user);

    InlineKeyboardMarkup chooseLanguage(BotUser botUser, boolean showBack);

    InlineKeyboardMarkup menuSetting(BotUser botUser);

    ReplyKeyboard sharePhone(BotUser botUser);

    ReplyKeyboard removeReplyKeyboard();

    ReplyKeyboard sendLocation(BotUser botUser);

    ReplyKeyboard checkAddress(BotUser botUser);

    ReplyKeyboard orderType(BotUser botUser);

    ReplyKeyboard sendLocationOrChooseAddress(BotUser botUser);

    ReplyKeyboard enterName(BotUser botUser);
}
