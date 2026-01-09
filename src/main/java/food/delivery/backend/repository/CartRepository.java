package food.delivery.backend.repository;

import food.delivery.backend.entity.Cart;
import food.delivery.backend.enums.CartStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by Avaz Absamatov
 * Date: 1/6/2026
 */
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByCreatedByAndStatus(Long createdBy, CartStatus status);
}
