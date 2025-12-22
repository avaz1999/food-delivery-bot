package food.delivery.backend.repository;

import food.delivery.backend.entity.User;
import food.delivery.backend.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by Avaz Absamatov
 * Date: 12/22/2025
 */
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String developerAvaz);
    Optional<User> findByUsernameAndStatus(String username, Status status);

}
