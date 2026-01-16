package food.delivery.backend.repository;

import food.delivery.backend.entity.Order;
import food.delivery.backend.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Avaz Absamatov
 * Date: 1/13/2026
 */
public interface OrderRepository extends JpaRepository<Order, Long> {
    boolean existsByOrderId(String orderId);

    List<Order> findAllByCreatedBy(Long createdBy);
}
