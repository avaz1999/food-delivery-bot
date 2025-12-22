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

    BOT_SHARE_PHONE_NUMBER("<b>Telefon raqmingizni yuboring</b> ☎️.\n<b>Yoki ushbu formatda</b> <code>(998*********)</code> <b> kiriting</b>",
            "<b>Отправьте ваш номер телефона</b> ☎️.\n<b>Или введите в этом формате</b> <code>(998********)</code>"),

    BOT_SHARE_PHONE_NUMBER_INVALID("<b>Telefon raqm yuborishda xatolik❓\nTelefon raqmingizni yuboring</b> ☎️.\n<b>Yoki ushbu formatda</b> <code>(998*********)</code> <b> kiriting</b>",
            "<b>Ошибка при отправке номера телефона❓\nОтправьте ваш номер телефона</b> ☎️.\n<b>Или введите в этом формате</b> <code>(998********)</code>"),

    INVALID_MESSAGE("Habar turida xatolik. ⚠️", "Ошибка типа сообщения. ⚠️"),

    INVALID_PHONE_NUMBER("Ushbu telefon raqam sizga tegishli emas.",
            "Этот номер телефона вам не принадлежит."),

    SUCCESS_CHANGE_PHONE_NUMBER("Telefon raqamingiz muvofaqiyatli o'zgartirildi.✅",
            "Ваш номер телефона успешно изменен.✅"),

    ADD_ADDRESS("Maznilingizni yuboring \uD83D\uDCCD.\nYoki maznilingizni kiriting",
            "Отправьте свой адрес \uD83D\uDCCD.\nИли введите свой адрес."),

    USER_ADDRESS("<b>Sizning manzilingiz to'g'rimi?</b>\n<b>{0}</b>",
            "<b>Ваш адрес указан верно?</b>\n<b>{0}</b>"),

    SAVED_ADDRESS("✅ Manzilingiz saqlandi.", "✅ Ваш адрес сохранен."),

    CHOOSE_ORDER_TYPE("Buyurtma turini tanlang.", "Выберите тип заказа"),

    SEND_LOCATION_OR_CHOOSE_ADDRESS("Yetkazib berish uchun geo-lokatisiya yuboring yoki manzilni tanlang",
            "Отправить данные о местоположении или выбрать адрес доставки"),

    ENTER_NAME("Ismingizni kiriting yoki tanlang.", "Введите или выберите свое имя."),

    ACCEPT_NAME("Ismingizni tasdiqlang \uD83D\uDE4E\uD83C\uDFFB\u200D♂️ {0Подтвердите свое имя \uD83D\uDE4E\uD83C\uDFFB\u200D♂️ {0}}", ""),

    CHOOSE_CATEGORY_ITEM("Mahsulot kategoriyalardan birini tanlang", "Выберите одну из категорий товаров.");


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
