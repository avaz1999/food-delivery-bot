package food.delivery.backend.repository;

import food.delivery.backend.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by Avaz Absamatov
 * Date: 1/6/2026
 */
public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findByCreatedBy(Long createdBy);
}
