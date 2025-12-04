package food.delivery.bot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * Created by Avaz Absamatov
 * Date: 11/30/2025
 */
@ConfigurationProperties(prefix = "telegram.bot")
public record BotProperties(
        String username,
        String token,
        String webhookPath,
        String botPath,
        List<String> allowedUpdates) {
}