package food.delivery.bot.service.base.impl;

import food.delivery.backend.dto.request.BotUserDTO;
import food.delivery.bot.service.base.ReplyMarkupService;
import food.delivery.bot.utils.BotCommands;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

/**
 * Created by Avaz Absamatov
 * Date: 12/2/2025
 */
@Service
@RequiredArgsConstructor
public class ReplyMarkupServiceImpl implements ReplyMarkupService {
    @Override
    public ReplyKeyboardMarkup mainMenu(BotUserDTO user) {
        return ReplyKeyboardMarkup.builder()
                .resizeKeyboard(true)
                .keyboard(List.of(
                        new KeyboardRow(List.of(
                                KeyboardButton.builder()
                                        .text(BotCommands.MENU_START_ORDER.getMessage(user.getLanguage()))
                                        .build()
                        )),
                        new KeyboardRow(List.of(
                                KeyboardButton.builder().text(BotCommands.MENU_ABOUT_US.getMessage(user.getLanguage())).build(),
                                KeyboardButton.builder().text(BotCommands.MENU_MY_ORDER.getMessage(user.getLanguage())).build()
                        )),
                        new KeyboardRow(List.of(
                                KeyboardButton.builder().text(BotCommands.MENU_COMMENT.getMessage(user.getLanguage())).build(),
                                KeyboardButton.builder().text(BotCommands.MENU_SETTINGS.getMessage(user.getLanguage())).build()
                        ))
                ))
                .build();
    }

    @Override
    public InlineKeyboardMarkup chooseLanguage() {

        InlineKeyboardButton uz = InlineKeyboardButton.builder()
                .text("O‘zbekcha (Lotin)")
                .callbackData("lang_uz")
                .build();

        InlineKeyboardButton uzCyr = InlineKeyboardButton.builder()
                .text("Ўзбекча (Кирилл)")
                .callbackData("lang_uz_cyr")
                .build();

        InlineKeyboardButton ru = InlineKeyboardButton.builder()
                .text("Русский")
                .callbackData("lang_ru")
                .build();

        InlineKeyboardButton en = InlineKeyboardButton.builder()
                .text("English")
                .callbackData("lang_en")
                .build();

        InlineKeyboardRow row1 = new InlineKeyboardRow();
        row1.add(uz);
        row1.add(uzCyr);

        InlineKeyboardRow row2 = new InlineKeyboardRow();
        row2.add(ru);
        row2.add(en);

        return InlineKeyboardMarkup.builder()
                .keyboard(List.of(row1, row2))
                .build();
    }


}
