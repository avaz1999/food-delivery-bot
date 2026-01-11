package food.delivery.backend.repository;

import food.delivery.backend.entity.CartItem;
import food.delivery.backend.enums.CartStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Created by Avaz Absamatov
 * Date: 1/6/2026
 */
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
                update CartItem ci
                set ci.status = :status
                where ci.cart.id = :cartId
            """)
    void updateStatusByCartId(@Param("cartId") Long cartId,
                              @Param("status") CartStatus status);

    Optional<CartItem> findByCartIdAndItemIdAndStatus(Long id, Long itemId, CartStatus cartStatus);

}
