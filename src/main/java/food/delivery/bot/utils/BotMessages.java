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
public enum BotMessages {
    BOT_START1("Assalomu aleykum ",
            "Здравствуйте, "),
    BOT_START2(" botga hush kelibsiz ✋", " добро пожаловать в бот. ✋"),
    BOT_CHOOSE_LANGUAGE("Tilni tanlang",
            "Выберите язык"),

    BOT_SHARE_PHONE_NUMBER("Telefon raqmizni yuboring",
            "Отправьте наш номер телефона");

    public String getMessage(Language language) {
        if (language.equals(Language.UZ)) return uz;
        else if (language.equals(Language.RU)) return ru;
        else return uz;
    }

    private final String uz;
    private final String ru;


}
