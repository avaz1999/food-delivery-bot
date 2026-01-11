package food.delivery.bot.service.base.impl;

import food.delivery.backend.entity.BotUser;
import food.delivery.backend.enums.Language;
import food.delivery.backend.model.dto.CartDTO;
import food.delivery.backend.model.dto.CartItemDTO;
import food.delivery.backend.model.dto.CategoryDTO;
import food.delivery.backend.service.CartService;
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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Avaz Absamatov
 * Date: 12/2/2025
 */
@Service
@RequiredArgsConstructor
public class ReplyMarkupServiceImpl implements ReplyMarkupService {
    private final CategoryService categoryService;
    private final CartService cartService;

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

        CategoryContext context = resolveCategoryContext(categoryId, isOne);

        List<CategoryDTO> categories = categoryService
                .getAllActiveCategory(context.searchParentId(), botUser.getLanguage());

        List<InlineKeyboardRow> rows = buildCategoryRows(categories);
        rows.addAll(buildBottomRows(botUser, context));

        return new InlineKeyboardMarkup(rows);
    }


    private CategoryContext resolveCategoryContext(Long categoryId, boolean isOne) {

        if (categoryId == null) {
            return new CategoryContext(null, false, null);
        }

        Long parentId = isOne
                ? categoryId
                : categoryService.getParentId(categoryId);

        boolean showBackButton = parentId != null;
        Long searchParentId = showBackButton ? parentId : null;
        Long backCategoryId = showBackButton ? parentId : null;

        return new CategoryContext(searchParentId, showBackButton, backCategoryId);
    }

    private List<InlineKeyboardRow> buildCategoryRows(List<CategoryDTO> categories) {

        List<InlineKeyboardRow> rows = new ArrayList<>();
        InlineKeyboardRow currentRow = new InlineKeyboardRow();

        for (CategoryDTO category : categories) {
            currentRow.add(buildCategoryButton(category));

            if (currentRow.size() == 2) {
                rows.add(currentRow);
                currentRow = new InlineKeyboardRow();
            }
        }

        if (!currentRow.isEmpty()) {
            rows.add(currentRow);
        }

        return rows;
    }

    private InlineKeyboardButton buildCategoryButton(CategoryDTO category) {
        return InlineKeyboardButton.builder()
                .text(category.getName())
                .callbackData("CATEGORY#" + category.getId())
                .build();
    }

    private List<InlineKeyboardRow> buildBottomRows(
            BotUser botUser,
            CategoryContext context
    ) {

        List<InlineKeyboardRow> rows = new ArrayList<>();

        rows.add(buildNavigationRow(botUser, context));

        if (cartService.getActiveCartByUser(botUser)) {
            rows.add(buildMyCartRow(botUser));
        }

        return rows;
    }

    private InlineKeyboardRow buildNavigationRow(
            BotUser botUser,
            CategoryContext context
    ) {

        InlineKeyboardRow row = new InlineKeyboardRow();

        if (context.showBackButton()) {
            row.add(
                    InlineKeyboardButton.builder()
                            .text(BotCommands.BACK.getMessage(botUser.getLanguage()))
                            .callbackData(BotCommands.BACK.name() + "#" + context.backCategoryId())
                            .build()
            );
        } else {
            row.add(
                    InlineKeyboardButton.builder()
                            .text(BotCommands.MAIN_MENU.getMessage(botUser.getLanguage()))
                            .callbackData(BotCommands.MAIN_MENU.name())
                            .build()
            );
        }

        return row;
    }

    private InlineKeyboardRow buildMyCartRow(BotUser botUser) {

        InlineKeyboardRow row = new InlineKeyboardRow();

        row.add(
                InlineKeyboardButton.builder()
                        .text(BotCommands.MY_ACTIVE_CART.getMessage(botUser.getLanguage()))
                        .callbackData(BotCommands.MY_ACTIVE_CART.name())
                        .build()
        );

        return row;
    }


    @Override
    public InlineKeyboardMarkup oneItemReply(BotUser botUser, Long categoryId, int count, Long itemId) {
        List<InlineKeyboardRow> rows = new ArrayList<>();
        InlineKeyboardRow row = new InlineKeyboardRow();
        InlineKeyboardRow back = new InlineKeyboardRow();
        InlineKeyboardRow cart = new InlineKeyboardRow();

        row.add(InlineKeyboardButton.builder()
                .text(BotCommands.MINUS.getMessage(botUser.getLanguage()))
                .callbackData(BotCommands.MINUS.name() + "#" + categoryId + "#" + count + "#" + itemId)
                .build());

        row.add(InlineKeyboardButton.builder()
                .text(String.valueOf(count))
                .callbackData(BotCommands.IGNORE.name())
                .build());

        row.add(InlineKeyboardButton.builder()
                .text(BotCommands.PLUS.getMessage(botUser.getLanguage()))
                .callbackData(BotCommands.PLUS.name() + "#" + categoryId + "#" + count + "#" + itemId)
                .build());

        back.add(InlineKeyboardButton.builder()
                .text(BotCommands.BACK.getMessage(botUser.getLanguage()))
                .callbackData(BotCommands.BACK.name() + "#" + categoryId)
                .build());

        cart.add(InlineKeyboardButton.builder()
                .text(BotCommands.ADD_CART.getMessage(botUser.getLanguage()))
                .callbackData(BotCommands.ADD_CART.name() + "#" + categoryId + "#" + count + "#" + itemId)
                .build());
        rows.add(row);
        rows.add(back);
        rows.add(cart);

        return new InlineKeyboardMarkup(rows);
    }

    @Override
    public InlineKeyboardMarkup cartMenu(BotUser botUser, CartDTO cart) {
        List<InlineKeyboardRow> rows = new ArrayList<>();

        rows.add(buildCartTopRow(botUser, cart));
        rows.add(buildClearCartRow(botUser, cart));
        rows.addAll(buildCartItemsRows(cart, botUser));

        return new InlineKeyboardMarkup(rows);
    }

    private InlineKeyboardRow buildCartTopRow(BotUser botUser, CartDTO cart) {

        InlineKeyboardRow row = new InlineKeyboardRow();

        row.add(
                InlineKeyboardButton.builder()
                        .text(BotCommands.BACK.getMessage(botUser.getLanguage()))
                        .callbackData(BotCommands.BACK.name())
                        .build()
        );

        row.add(
                InlineKeyboardButton.builder()
                        .text(BotCommands.ADD_ORDER.getMessage(botUser.getLanguage()))
                        .callbackData(BotCommands.ADD_ORDER.name() + "#" + cart.getId())
                        .build()
        );

        return row;
    }

    private InlineKeyboardRow buildClearCartRow(BotUser botUser, CartDTO cart) {

        InlineKeyboardRow row = new InlineKeyboardRow();

        row.add(
                InlineKeyboardButton.builder()
                        .text(BotCommands.CLEAR_CART.getMessage(botUser.getLanguage()))
                        .callbackData(BotCommands.CLEAR_CART.name() + "#" + cart.getId())
                        .build()
        );

        return row;
    }

    private List<InlineKeyboardRow> buildCartItemsRows(CartDTO cart, BotUser botUser) {

        return cart.getItems().stream()
                .collect(Collectors.toMap(
                        CartItemDTO::getItemName,
                        item -> item,
                        (a, b) -> a
                ))
                .values()
                .stream()
                .sorted(Comparator.comparing(CartItemDTO::getItemName))
                .map(item -> buildSingleCartItemRow(item, botUser))
                .toList();
    }


    private InlineKeyboardRow buildSingleCartItemRow(CartItemDTO item, BotUser botUser) {

        InlineKeyboardRow row = new InlineKeyboardRow();

        row.add(
                InlineKeyboardButton.builder()
                        .text(BotCommands.MINUS.getMessage(botUser.getLanguage()))
                        .callbackData(BotCommands.MINUS.name() + "#" + item.getId())
                        .build()
        );

        row.add(
                InlineKeyboardButton.builder()
                        .text(item.getItemName())
                        .callbackData("CART_ITEM_INFO")
                        .build()
        );


        row.add(
                InlineKeyboardButton.builder()
                        .text(BotCommands.PLUS.getMessage(botUser.getLanguage()))
                        .callbackData(BotCommands.PLUS.name() + "#" + item.getId())
                        .build()
        );

        return row;
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

    private record CategoryContext(
            Long searchParentId,
            boolean showBackButton,
            Long backCategoryId
    ) {
    }


}
