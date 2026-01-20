package food.delivery.backend.repository;

import food.delivery.backend.entity.Order;
import food.delivery.backend.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Avaz Absamatov
 * Date: 1/13/2026
 */
public interface OrderRepository extends JpaRepository<Order, Long> {
    boolean existsByOrderId(String orderId);

    Page<Order> findAllByCreatedBy(Long createdBy, Pageable pageable);

    Order findByCreatedByAndStatus(Long createdBy, OrderStatus status);
}
