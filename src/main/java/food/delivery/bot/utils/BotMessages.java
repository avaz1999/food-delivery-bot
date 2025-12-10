package food.delivery.bot.utils;

import food.delivery.backend.enums.Language;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.text.MessageFormat;

/**
 * Created by Avaz Absamatov
 * Date: 12/3/2025
 */
@Getter
@RequiredArgsConstructor
public enum BotMessages {
    WELCOME("Assalomu alaykum, {0}! botga hush kelibsiz ✋",
            "Здравствуйте, {0}! добро пожаловать в бот. ✋"),

    CHOOSE_LANGUAGE("Tilni tanlang", "Выберите язык"),

    ADD_ORDER_QUESTION("Buyurtmani birga joylashtiramizmi? \uD83E\uDD17",
            "Можем ли мы оформить заказ вместе? \uD83E\uDD17"),

    ADD_ORDER("Buyurtma berishni boshlash uchun \uD83D\uDED2 Buyurtma qilish tugmasini bosing",
            "Чтобы начать оформление заказа, нажмите кнопку \uD83D\uDED2 Заказать"),

    SETTING_MENU("TIL: {0}.\nTELEFON: {1}.\nMANZIL: {2}.\n\nQuyidagilardan birini tanlang.",
            "ЯЗЫК: {0}.\nТЕЛЕФОН: {1}.\nАДРЕС: {2}.\n\nВыберите один из следующих вариантов."),

    BOT_SHARE_PHONE_NUMBER("Telefon raqmizni yuboring ☎️",
            "Отправьте ваш номер телефона ☎️");


    public String getMessage(Language language) {
        if (language.equals(Language.UZ)) return uz;
        else if (language.equals(Language.RU)) return ru;
        else return uz;
    }

    public String getMessageWPar(Language lang, Object... params) {
        String template = switch (lang) {
            case UZ -> uz;
            case EN -> null;
            case RU -> ru;
        };
        if (params.length > 0) {
            assert template != null;
            return MessageFormat.format(template, params);
        } else {
            return template;
        }
    }

    private final String uz;
    private final String ru;


}
