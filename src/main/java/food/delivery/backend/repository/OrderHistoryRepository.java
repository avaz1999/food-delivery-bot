package food.delivery.backend.repository;

import food.delivery.backend.entity.OrderHistory;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Avaz Absamatov
 * Date: 1/13/2026
 */
public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Long> {
}
