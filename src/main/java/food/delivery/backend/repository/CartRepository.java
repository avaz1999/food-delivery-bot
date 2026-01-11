package food.delivery.backend.repository;

import food.delivery.backend.entity.Cart;
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
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByCreatedByAndStatus(Long createdBy, CartStatus status);

    boolean existsByCreatedByAndStatus(Long createdBy, CartStatus status);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
                update Cart c
                set c.status = :status
                where c.id = :cartId
            """)
    void updateStatus(@Param("cartId") Long cartId,
                      @Param("status") CartStatus status);


}
