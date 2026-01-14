package food.delivery.backend.repository;

import food.delivery.backend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Avaz Absamatov
 * Date: 1/13/2026
 */
public interface OrderRepository extends JpaRepository<Order, Long> {
    boolean existsByOrderId(String orderId);
}
