package food.delivery.bot.service.base.impl;

import food.delivery.backend.dto.request.BotUserDTO;
import food.delivery.backend.enums.Language;
import food.delivery.bot.service.base.ReplyMarkupService;
import food.delivery.bot.utils.BotCommands;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.List;

/**
 * Created by Avaz Absamatov
 * Date: 12/2/2025
 */
@Service
@RequiredArgsConstructor
public class ReplyMarkupServiceImpl implements ReplyMarkupService {
    @Override
    public InlineKeyboardMarkup mainMenu(BotUserDTO user) {
        return InlineKeyboardMarkup.builder()
                .keyboard(List.of(
                        new InlineKeyboardRow(
                                InlineKeyboardButton.builder()
                                        .text(BotCommands.MENU_START_ORDER.getMessage(user.getLanguage()))
                                        .callbackData(BotCommands.MENU_START_ORDER.name())
                                        .build()
                        ),

                        new InlineKeyboardRow(
                                InlineKeyboardButton.builder()
                                        .text(BotCommands.MENU_ABOUT_US.getMessage(user.getLanguage()))
                                        .callbackData(BotCommands.MENU_ABOUT_US.name())
                                        .build(),
                                InlineKeyboardButton.builder()
                                        .text(BotCommands.MENU_MY_ORDER.getMessage(user.getLanguage()))
                                        .callbackData(BotCommands.MENU_MY_ORDER.name())
                                        .build()
                        ),

                        new InlineKeyboardRow(
                                InlineKeyboardButton.builder()
                                        .text(BotCommands.MENU_COMMENT.getMessage(user.getLanguage()))
                                        .callbackData(BotCommands.MENU_COMMENT.name())
                                        .build(),
                                InlineKeyboardButton.builder()
                                        .text(BotCommands.MENU_SETTINGS.getMessage(user.getLanguage()))
                                        .callbackData(BotCommands.MENU_SETTINGS.name())
                                        .build()
                        )
                ))
                .build();
    }

    @Override
    public InlineKeyboardMarkup chooseLanguage(BotUserDTO botUserDTO, boolean showBack) {

        InlineKeyboardButton uz = InlineKeyboardButton.builder()
                .text("\uD83C\uDDFA\uD83C\uDDFF") // 🇺🇿
                .callbackData(Language.UZ.name())
                .build();

        InlineKeyboardButton ru = InlineKeyboardButton.builder()
                .text("\uD83C\uDDF7\uD83C\uDDFA") // 🇷🇺
                .callbackData(Language.RU.name())
                .build();

        InlineKeyboardRow row1 = new InlineKeyboardRow(List.of(uz, ru));

        if (!showBack) {
            return InlineKeyboardMarkup.builder()
                    .keyboard(List.of(row1))
                    .build();
        }
        InlineKeyboardButton back = InlineKeyboardButton.builder()
                .text(BotCommands.BACK.getMessage(botUserDTO.getLanguage()))
                .callbackData(BotCommands.BACK.name())
                .build();

        InlineKeyboardRow row2 = new InlineKeyboardRow(List.of(back));

        return InlineKeyboardMarkup.builder()
                .keyboard(List.of(row1, row2))
                .build();
    }

    @Override
    public InlineKeyboardMarkup menuSetting(BotUserDTO user) {
        return InlineKeyboardMarkup.builder()
                .keyboard(List.of(
                        new InlineKeyboardRow(
                                InlineKeyboardButton.builder()
                                        .text(BotCommands.SETTINGS_MENU_CHANGE_LANG.getMessage(user.getLanguage()))
                                        .callbackData(BotCommands.SETTINGS_MENU_CHANGE_LANG.name())
                                        .build(),
                                InlineKeyboardButton.builder()
                                        .text(BotCommands.SETTINGS_MENU_CHANGE_PHONE.getMessage(user.getLanguage()))
                                        .callbackData(BotCommands.SETTINGS_MENU_CHANGE_PHONE.name())
                                        .build(),
                                InlineKeyboardButton.builder()
                                        .text(BotCommands.SETTINGS_MENU_CHANGE_ADDRESS.getMessage(user.getLanguage()))
                                        .callbackData(BotCommands.SETTINGS_MENU_CHANGE_ADDRESS.name())
                                        .build()
                        ),

                        new InlineKeyboardRow(
                                InlineKeyboardButton.builder()
                                        .text(BotCommands.MAIN_MENU.getMessage(user.getLanguage()))
                                        .callbackData(BotCommands.MAIN_MENU.name())
                                        .build()
                        )
                ))
                .build();
    }


}
