package food.delivery.backend.config;

import food.delivery.backend.model.dto.AuthPrincipalDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Optional;

/**
 * Created by Avaz Absamatov
 * Date: 11/24/2025
 */
@Slf4j
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@EnableTransactionManagement
public class JPAAuditConfig {

    @Bean
    public AuditorAware<Long> auditorProvider() {
        return () -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated()
                    || "anonymousUser".equals(authentication.getName())) {
                return Optional.of(0L);
            }

            Object principal = authentication.getPrincipal();

            if (principal instanceof AuthPrincipalDTO dto) {
                return Optional.of(dto.id());
            }

            if (principal instanceof Long id) {
                return Optional.of(id);
            }

            if (principal instanceof String str) {
                try {
                    String idStr = str.replaceAll(".*id[=:](\\d+).*", "$1");
                    return Optional.of(Long.parseLong(idStr));
                } catch (Exception e) {
                    log.warn("Could not parse user ID from principal: {}", str);
                }
            }

            return Optional.of(0L);
        };
    }
}
