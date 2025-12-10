package food.delivery.bot.service.base;

import food.delivery.backend.dto.request.BotUserDTO;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

/**
 * Created by Avaz Absamatov
 * Date: 12/2/2025
 */
public interface ReplyMarkupService {
    InlineKeyboardMarkup mainMenu(BotUserDTO user);

    InlineKeyboardMarkup chooseLanguage(BotUserDTO botUserDTO, boolean showBack);

    InlineKeyboardMarkup menuSetting(BotUserDTO botUserDTO);
}
