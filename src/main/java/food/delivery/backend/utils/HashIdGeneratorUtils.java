package food.delivery.backend.utils;

import lombok.experimental.UtilityClass;

import java.util.UUID;

/**
 * Created by Avaz Absamatov
 * Date: 12/27/2025
 */
@UtilityClass
public class HashIdGeneratorUtils {
    private static final String BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public static String generateHashId() {
        long value = System.nanoTime() ^ UUID.randomUUID().getMostSignificantBits();

        StringBuilder sb = new StringBuilder();
        while (value > 0) {
            sb.append(BASE62.charAt((int) (value % 62)));
            value /= 62;
        }
        return sb.toString();
    }
}
