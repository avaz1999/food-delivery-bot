package food.delivery.backend.security;

import com.auth0.jwt.algorithms.Algorithm;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Avaz Absamatov
 * Date: 12/22/2025
 */
@Configuration
@ConfigurationProperties(prefix = "app.jwt")
@Getter
@Setter
public class JwtProperties {
    private String secret;
    private long accessTokenExpiryMs;
    private long refreshTokenExpiryMs;

    public Algorithm getAlgorithm() {
        return Algorithm.HMAC256(secret.getBytes());
    }
}