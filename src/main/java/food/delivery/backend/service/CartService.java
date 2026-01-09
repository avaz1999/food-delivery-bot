package food.delivery.backend.service;

import food.delivery.backend.entity.Cart;
import food.delivery.backend.enums.CartStatus;
import food.delivery.backend.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Created by Avaz Absamatov
 * Date: 1/6/2026
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository repository;

    public Cart getOrCreateActiveCart(Long userId) {
        return repository.findByCreatedByAndStatus(userId, CartStatus.ACTIVE)
                .orElseGet(() -> {
                    Cart newCart = Cart.builder()
                            .status(CartStatus.ACTIVE)
                            .totalPrice(BigDecimal.ZERO)
                            .createdAt(LocalDateTime.now())
                            .createdBy(userId)
                            .build();

                    newCart.setCreatedBy(userId);

                    return repository.save(newCart);
                });
    }

    public void save(Cart cart) {
        repository.save(cart);
    }
}
