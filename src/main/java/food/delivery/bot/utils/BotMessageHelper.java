package food.delivery.bot.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BotMessageHelper {

    // Bir marta yaratiladi, thread-safe
    private static final ReloadableResourceBundleMessageSource messageSource;

    static {
        messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
        messageSource.setFallbackToSystemLocale(false);
        messageSource.setUseCodeAsDefaultMessage(false); // topilmasa null qaytarmaslik uchun
    }

    /**
     * O‘zbek tilini to‘g‘ri yo‘naltirish:
     * uz + Cyrl → messages_la.properties (kirill)
     * uz + Latn yoki bo‘sh → messages_uz.properties (lotin)
     */
    private static Locale resolveLocale(Locale locale) {
        if (locale == null) {
            return new Locale("uz"); // default
        }

        if ("uz".equalsIgnoreCase(locale.getLanguage())) {
            String script = locale.getScript();
            if ("Cyrl".equalsIgnoreCase(script)) {
                return new Locale("la");        // kirill uchun
            }
            // Latn yoki bo‘sh bo‘lsa → lotin
            return new Locale("uz");
        }

        return locale; // boshqa tillar o‘z holicha
    }

    // =========================== JAVOB METODLARI ===========================

    /** Parametrsiz */
    public static String get(String code) {
        return get(code, new Object[0]);
    }

    /** 1 ta parametr: get("bot.start", userName) */
    public static String get(String code, Object arg1) {
        return get(code, new Object[]{arg1});
    }

    /** 2 ta parametr */
    public static String get(String code, Object arg1, Object arg2) {
        return get(code, new Object[]{arg1, arg2});
    }

    /** 3 ta parametr */
    public static String get(String code, Object arg1, Object arg2, Object arg3) {
        return get(code, new Object[]{arg1, arg2, arg3});
    }

    /** Istagan miqdorda parametr (eng qulay) */
    public static String get(String code, Object... args) {
        try {
            Locale locale = resolveLocale(LocaleContextHolder.getLocale());
            return messageSource.getMessage(code, args, locale);
        } catch (Exception e) {
            // Debug uchun juda qulay: qaysi kod va parametrlar ishlamaganini ko‘rish mumkin
            String params = args.length > 0 ? " [" + String.join(", ",
                    java.util.Arrays.stream(args).map(Object::toString).toList()) + "]" : "";
            return code + params; // masalan: bot.start [Avaz]
        }
    }
}