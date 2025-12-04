package food.delivery.backend.repository;

import food.delivery.backend.entity.BotUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Avaz Absamatov
 * Date: 12/2/2025
 */
public interface BotUserRepository extends JpaRepository<BotUser, Long> {
    BotUser findByUserId(Long userId);
}
