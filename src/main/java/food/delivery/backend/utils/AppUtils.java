package food.delivery.backend.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import org.springframework.web.servlet.HandlerMapping;

/**
 * Created by Avaz Absamatov
 * Date: 12/22/2025
 */
@UtilityClass
public class AppUtils {
    public static final String BASE_URL_V1 = "/v1/api/";

    public boolean isPublicEndpoint(String path) {
        return path.startsWith("/swagger-ui") ||
                path.startsWith("/v3/api-docs") ||
                path.startsWith("/webjars") ||
                path.equals("/api/sign-in") ||
                path.startsWith("/actuator");
    }

    public String cleanPath(HttpServletRequest request) {
        String pattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);

        if (pattern != null && !pattern.contains("{") && !pattern.contains("}"))
            return pattern;

        String uri = request.getRequestURI();

        return uri.replaceAll("/\\d+([/?]|$)", "/{id}$1")
                .replaceAll("/[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}([/?]|$)", "/{uuid}$1");
    }
}