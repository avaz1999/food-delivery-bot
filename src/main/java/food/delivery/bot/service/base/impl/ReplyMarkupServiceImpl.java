package food.delivery.bot.service.base.impl;

import food.delivery.backend.entity.BotUser;
import food.delivery.backend.enums.Language;
import food.delivery.backend.model.dto.CategoryDTO;
import food.delivery.backend.service.CategoryService;
import food.delivery.bot.service.base.ReplyMarkupService;
import food.delivery.bot.utils.BotCommands;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Avaz Absamatov
 * Date: 12/2/2025
 */
@Service
@RequiredArgsConstructor
public class ReplyMarkupServiceImpl implements ReplyMarkupService {
    private final CategoryService categoryService;

    @Override
    public InlineKeyboardMarkup mainMenu(BotUser user) {
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
    public InlineKeyboardMarkup chooseLanguage(BotUser botUser, boolean showBack) {

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
                .text(BotCommands.BACK.getMessage(botUser.getLanguage()))
                .callbackData(BotCommands.BACK.name())
                .build();

        InlineKeyboardRow row2 = new InlineKeyboardRow(List.of(back));

        return InlineKeyboardMarkup.builder()
                .keyboard(List.of(row1, row2))
                .build();
    }

    @Override
    public InlineKeyboardMarkup menuSetting(BotUser user) {
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

    @Override
    public ReplyKeyboard sharePhone(BotUser botUser) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(
                List.of(
                        new KeyboardRow(List.of(
                                KeyboardButton.builder()
                                        .text(BotCommands.SHARE_PHONE.getMessage(botUser.getLanguage()))
                                        .requestContact(true)
                                        .build()
                        )),
                        new KeyboardRow(List.of(
                                KeyboardButton.builder()
                                        .text(BotCommands.MAIN_MENU.getMessage(botUser.getLanguage()))
                                        .build()
                        ))
                )
        );
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        return replyKeyboardMarkup;
    }

    @Override
    public ReplyKeyboard removeReplyKeyboard() {
        return ReplyKeyboardRemove.builder()
                .removeKeyboard(true)
                .selective(false)
                .build();
    }

    @Override
    public ReplyKeyboard sendLocation(BotUser botUser) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(
                List.of(
                        new KeyboardRow(List.of(
                                KeyboardButton.builder()
                                        .text(BotCommands.SHARE_ADDRESS.getMessage(botUser.getLanguage()))
                                        .requestLocation(true)
                                        .build()
                        )),
                        new KeyboardRow(List.of(
                                KeyboardButton.builder()
                                        .text(BotCommands.MAIN_MENU.getMessage(botUser.getLanguage()))
                                        .build()
                        ))
                )
        );
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        return replyKeyboardMarkup;

    }

    @Override
    public ReplyKeyboard confirmData(BotUser botUser) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(
                List.of(
                        new KeyboardRow(List.of(
                                KeyboardButton.builder()
                                        .text(BotCommands.CHECK_YES.getMessage(botUser.getLanguage()))
                                        .build(),
                                KeyboardButton.builder()
                                        .text(BotCommands.CHECK_NO.getMessage(botUser.getLanguage()))
                                        .build()
                        ))
                )
        );
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        return replyKeyboardMarkup;
    }

    @Override
    public ReplyKeyboard orderType(BotUser botUser) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(
                List.of(
                        new KeyboardRow(List.of(
                                KeyboardButton.builder()
                                        .text(BotCommands.ORDER_DELIVERY.getMessage(botUser.getLanguage()))
                                        .build(),
                                KeyboardButton.builder()
                                        .text(BotCommands.ORDER_PICKUP.getMessage(botUser.getLanguage()))
                                        .build()
                        )),
                        new KeyboardRow(List.of(
                                KeyboardButton.builder()
                                        .text(BotCommands.MAIN_MENU.getMessage(botUser.getLanguage()))
                                        .build()
                        ))
                )
        );
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        return replyKeyboardMarkup;
    }

    @Override
    public ReplyKeyboard sendLocationOrChooseAddress(BotUser botUser) {
        List<KeyboardRow> rows = new ArrayList<>();

        rows.add(new KeyboardRow(List.of(
                KeyboardButton.builder()
                        .text(BotCommands.SHARE_ADDRESS.getMessage(botUser.getLanguage()))
                        .requestLocation(true)
                        .build()
        )));

        return buildKeyboardWithSavedValue(botUser, botUser.getAddress(), rows);
    }

    @Override
    public ReplyKeyboard enterName(BotUser botUser) {
        List<KeyboardRow> rows = new ArrayList<>();

        String savedFullName = botUser.getFullName();
        return buildKeyboardWithSavedValue(botUser, savedFullName, rows);
    }

    @Override
    public InlineKeyboardMarkup itemCategory(BotUser botUser, Long categoryId, boolean isOne) {

        Long searchParentId;
        boolean showBackButton;

        if (categoryId == null) {
            searchParentId = null;
            showBackButton = false;
        } else {
            Long parentId;
            if (!isOne) {
                parentId = categoryService.getParentId(categoryId);
            } else {
                parentId = categoryId;
            }
            if (parentId != null) categoryId = parentId;
            searchParentId = parentId == null ? null : categoryId;
            showBackButton = parentId != null;
        }

        List<CategoryDTO> list =
                categoryService.getAllActiveCategory(searchParentId, botUser.getLanguage());

        List<InlineKeyboardRow> rows = new ArrayList<>();
        InlineKeyboardRow row = new InlineKeyboardRow();

        int i = 0;
        for (CategoryDTO dto : list) {
            row.add(
                    InlineKeyboardButton.builder()
                            .text(dto.getName())
                            .callbackData("CATEGORY#" + dto.getId())
                            .build()
            );
            i++;
            if (i % 2 == 0) {
                rows.add(row);
                row = new InlineKeyboardRow();
            }
        }
        if (!row.isEmpty()) rows.add(row);

        InlineKeyboardRow bottomRow = new InlineKeyboardRow();

        if (showBackButton) {
            bottomRow.add(
                    InlineKeyboardButton.builder()
                            .text(BotCommands.BACK.getMessage(botUser.getLanguage()))
                            .callbackData(BotCommands.BACK.name() + "#" + categoryId)
                            .build()
            );
        } else {
            bottomRow.add(
                    InlineKeyboardButton.builder()
                            .text(BotCommands.MAIN_MENU.getMessage(botUser.getLanguage()))
                            .callbackData(BotCommands.MAIN_MENU.name())
                            .build()
            );
        }

        rows.add(bottomRow);
        return new InlineKeyboardMarkup(rows);
    }


    @NotNull
    private ReplyKeyboardMarkup buildKeyboardWithSavedValue(BotUser botUser, String savedValue, List<KeyboardRow> rows) {
        if (savedValue != null && !savedValue.trim().isEmpty()) {
            rows.add(new KeyboardRow(List.of(
                    KeyboardButton.builder()
                            .text("✅ " + savedValue)
                            .build()
            )));
        }

        rows.add(new KeyboardRow(List.of(
                KeyboardButton.builder()
                        .text(BotCommands.BACK.getMessage(botUser.getLanguage()))
                        .build()
        )));

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(rows);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        return replyKeyboardMarkup;
    }


}
