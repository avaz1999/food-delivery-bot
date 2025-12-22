package food.delivery.backend.config;

import food.delivery.backend.entity.User;
import food.delivery.backend.enums.Role;
import food.delivery.backend.enums.Status;
import food.delivery.backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Avaz Absamatov
 * Date: 12/22/2025
 */
@Slf4j
@Configuration
@AllArgsConstructor
public class InitialUserSetUp {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    @Transactional
    public CommandLineRunner init() {
        return args -> {
            if (!userRepository.existsByUsername("developerAvaz")) {
                User user = new User();
                user.setFirstName("Avaz");
                user.setLastName("Absamatov");
                user.setPassword(passwordEncoder.encode("developer!@#213"));
                user.setUsername("developerAvaz");
                user.setPhone("998999701899");
                user.setDeveloper(true);
                user.setRole(Role.DEVELOPER);
                user.setStatus(Status.ACTIVE);
                userRepository.save(user);
            }
        };
    }
}
