package food.delivery.backend.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

/**
 * MessageHelper - faqat o‘zbek va rus tillari uchun
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageHelper {

    private static MessageSource getMessageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
        return messageSource;
    }

    /**
     * Locale ni o‘zbek (lotin/kirill) va rus tiliga moslashtirish
     */
    private static Locale normalizeLocale(Locale locale) {
        String lang = locale.getLanguage();
        String script = locale.getScript();

        if ("uz".equals(lang)) {
            // O‘zbek lotin (default) yoki kirill
            return "Cyrl".equals(script)
                    ? Locale.of("uz", "", "Cyrl")
                    : Locale.of("uz");
        } else if ("ru".equals(lang)) {
            return Locale.of("ru");
        }

        // Default — rus tiliga o‘tkazamiz (yoki o‘zbek lotin)
        return Locale.of("ru"); // yoki "uz" — qaysi tilni default qilmoqchi bo‘lsangiz
    }

    private static String getMessage(String code, Object[] args, Locale locale) {
        try {
            return getMessageSource().getMessage(code, args, normalizeLocale(locale));
        } catch (Exception e) {
            return code; // agar xato bo‘lsa — kodni o‘zi qaytaradi
        }
    }

    public static String get(String code) {
        return getMessage(code, null, LocaleContextHolder.getLocale());
    }

    public static String get(String code, Object... args) {
        return getMessage(code, args, LocaleContextHolder.getLocale());
    }

    public static String get(String code, List<String> params) {
        return getMessage(code, params.toArray(new String[0]), LocaleContextHolder.getLocale());
    }
}