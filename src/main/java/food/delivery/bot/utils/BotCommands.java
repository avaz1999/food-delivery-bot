package food.delivery.bot.utils;

import food.delivery.backend.enums.Language;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created by Avaz Absamatov
 * Date: 12/3/2025
 */
@Getter
@RequiredArgsConstructor
public enum BotCommands {
    MAIN_MENU("🏠 ASOSIY MENU", "🏠 ГЛАВНОЕ МЕНЮ"),
    MENU_START_ORDER("Buyurtma berish \uD83D\uDED2", "Заказ \uD83D\uDED2"),
    MENU_ABOUT_US("Biz haqimizda ℹ️", "О нас ℹ️"),
    MENU_MY_ORDER("Mening buyurtmalarim \uD83D\uDECD", "Мои заказы \uD83D\uDECD"),
    MENU_COMMENT("Fikr bildirish ✍️", "Комментарий ✍️"),

    MENU_SETTINGS("Sozlamalar ⚙️", "Настройки ⚙️"),
    SETTINGS_MENU_CHANGE_LANG("Til", "Язык"),
    SETTINGS_MENU_CHANGE_ADDRESS("Manzil", "Адрес"),
    SETTINGS_MENU_CHANGE_PHONE("Telefon", "Телефон"),

    BACK("ORTGA ⬅️", "НАЗАД ⬅️"),
    SHARE_PHONE("RAQAMNI ULASHISH ☎️", "ПОДЕЛИТЕСЬ НОМЕРОМ ☎️"),

    SHARE_ADDRESS("\uD83D\uDCCD LOCATSIYA YUBORISH",
            "\uD83D\uDCCD ОТПРАВИТЬ МЕСТОПОЛОЖЕНИЕ"),

    CHECK_YES("✅ HA", "✅ ДА"),

    CHECK_NO("❌ YO'Q", "❌️ НЕТ"),

    ORDER_DELIVERY("\uD83D\uDE95 YETKAZIB BERISH",
            "\uD83D\uDE95 ДОСТАВКА"),

    ORDER_PICKUP("\uD83D\uDEB6 OLIB KETISH", "\uD83D\uDEB6 САМОВЫВОЗ"),

    PLUS("➕", "➕"),

    MINUS("➖", "➖"),

    ADD_CART("\uD83D\uDED2 Savatga qo'shish", "\uD83D\uDED2 Добавить в корзину"),

    IGNORE("IGNORE", "IGNORE"),

    MY_ACTIVE_CART("\uD83D\uDED2 Savatcha", "\uD83D\uDED2 Корзина"),

    ADD_ORDER("\uD83D\uDE96 BUYURTMA BERISH", "\uD83D\uDE96 СДЕЛАТЬ ЗАКАЗ"),

    CLEAR_CART("🗑 SAVATCHANI BO'SHATISH", "\uD83D\uDDD1 Опустошение корзины"),

    CASH("\uD83D\uDCB8 Naqt pul", "\uD83D\uDCB8 Наличные"),

    PAYMENT_TYPE("\uD83D\uDCB3 Click", "\uD83D\uDCB3 Click"),

    CLOSE("❌ YOPISH", "❌ ЗАКРЫТИЕ"),

    CANCEL("❌ Bekor qilish", "❌ Отмена"),

    ACCEPT("✅ Qabul qilishi", "✅ Принятие"),

    PREV("◀️", "◀️"),

    NEXT("➡️", "➡️"),;
    private final String uz;
    private final String ru;

    public String getMessage(Language language) {
        if (language.equals(Language.UZ)) return uz;
        else if (language.equals(Language.RU)) return ru;
        else return uz;
    }
}
