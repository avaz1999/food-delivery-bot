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
    MAIN_MENU("ASOSIY MENU ⬅️", "ГЛАВНОЕ МЕНЮ ⬅️"),
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

    SHARE_ADDRESS("\uD83D\uDCCD LOCATSIYA YUBORISH","\uD83D\uDCCD ОТПРАВИТЬ МЕСТОПОЛОЖЕНИЕ")
    ;
    private final String uz;
    private final String ru;

    public String getMessage(Language language) {
        if (language.equals(Language.UZ)) return uz;
        else if (language.equals(Language.RU)) return ru;
        else return uz;
    }
}
