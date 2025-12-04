package food.delivery.bot.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created by Avaz Absamatov
 * Date: 12/2/2025
 */
@Getter
@RequiredArgsConstructor
public enum ResponseCodes {
    SUCCESS(0, "success"),
    BOT_START(100, "bot.start"),
    ;
    private final Integer code; // response code
    private final String message;
}
