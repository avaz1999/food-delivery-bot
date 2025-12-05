package food.delivery.bot.config;

import food.delivery.bot.controller.BotController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.updates.DeleteWebhook;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.webhook.starter.SpringTelegramWebhookBot;

/**
 * Created by Avaz Absamatov
 * Date: 11/30/2025
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class TelegramConfig {
    private final BotProperties props;
    private final BotController handler;


    @Bean
    public SpringTelegramWebhookBot webhookBot(OkHttpTelegramClient client) {
        Runnable setWebhook = () -> {
            try {
                String externalUrl = props.webhookPath() + (props.webhookPath().endsWith("/") ? "" : "/") + props.botPath();
                var b = SetWebhook.builder().url(externalUrl);
                if (props.allowedUpdates() != null && !props.allowedUpdates().isEmpty()) {
                    b.allowedUpdates(props.allowedUpdates());
                }
                client.execute(b.build());
                log.info("Webhook set: {}", externalUrl);
            } catch (Exception e) {
                log.error("setWebhook failed: {}", e.getMessage(), e);
            }
        };
        // deleteWebhook ham o‘sha-o‘sha
        return SpringTelegramWebhookBot.builder()
                .botPath(props.botPath())         // => POST /webhook
                .updateHandler(update -> {
                    try {
                        handler.handle(update);
                    } catch (Exception e) {
                        log.error("Update handler failed: {}", e.getMessage(), e);
                    }
                    return null;
                })
                .setWebhook(setWebhook)
                .deleteWebhook(() -> {
                    try {
                        client.execute(DeleteWebhook.builder().build());
                    } catch (Exception ex) {
                        log.error("deleteWebhook failed: {}", ex.getMessage(), ex);
                    }
                })
                .build();
    }

}
