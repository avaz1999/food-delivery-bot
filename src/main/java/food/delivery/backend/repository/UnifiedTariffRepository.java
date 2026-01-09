package food.delivery.backend.repository;

import food.delivery.backend.entity.UnifiedTariff;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Avaz Absamatov
 * Date: 1/7/2026
 */
public interface UnifiedTariffRepository extends JpaRepository<UnifiedTariff, Long> {
}
