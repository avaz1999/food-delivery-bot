package food.delivery.bot.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;

/**
 * Created by Avaz Absamatov
 * Date: 12/5/2025
 */
@Configuration
@RequiredArgsConstructor
public class BotConfig {
    private final BotProperties props;
    @Bean
    public OkHttpTelegramClient telegramClient() {
        return new OkHttpTelegramClient(props.token());
    }
}
