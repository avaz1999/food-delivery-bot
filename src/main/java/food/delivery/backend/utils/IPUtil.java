package food.delivery.backend.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;

/**
 * Created by Avaz Absamatov
 * Date: 12/22/2025
 */
@UtilityClass
public class IPUtil {
    public static String getClientIp(HttpServletRequest request) {
        String xff = request.getHeader("X-Forwarded-For");
        if (xff != null && !xff.isBlank()) {
            return xff.split(",")[0].trim();
        }
        String xRealIp = request.getHeader("XRealIP");
        if (isValidHeader(xRealIp)) {
            return xRealIp.trim();
        }

        // Cloudflare, AWS ELB
        String cfConnectingIp = request.getHeader("CF-Connecting-IP");
        if (isValidHeader(cfConnectingIp)) {
            return cfConnectingIp.trim();
        }

        String remoteAddr = request.getRemoteAddr();
        return remoteAddr != null ? remoteAddr : "unknown";
    }

    private static boolean isValidHeader(String header) {
        return header != null && !header.isBlank() && !"unknown".equalsIgnoreCase(header.trim());
    }
}