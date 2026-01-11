package food.delivery.backend.service;

import food.delivery.backend.entity.BotUser;
import food.delivery.backend.entity.Cart;
import food.delivery.backend.entity.CartItem;
import food.delivery.backend.enums.CartStatus;
import food.delivery.backend.model.dto.CartDTO;
import food.delivery.backend.model.mapper.CartItemMapper;
import food.delivery.backend.repository.CartRepository;
import jakarta.transaction.Transactional;
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
    private final CartItemMapper cartItemMapper;

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

    public CartDTO saveCart(Cart cart) {
        repository.save(cart);
        return buildCartDTO(cart);
    }

    public boolean getActiveCartByUser(BotUser botUser) {
        return repository.existsByCreatedByAndStatus(botUser.getId(), CartStatus.ACTIVE);
    }

    public CartDTO getCartByUser(BotUser botUser) {
        return repository.findByCreatedByAndStatus(botUser.getId(), CartStatus.ACTIVE)
                .map(this::buildCartDTO).orElse(null);
    }

    private CartDTO buildCartDTO(Cart cart) {
        BigDecimal itemsSum = cart.getItems()
                .stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return CartDTO.builder()
                .id(cart.getId())
                .items(cartItemMapper.toDTOList(cart.getItems()))
                .itemsPrice(itemsSum)
                .deliveryPrice(cart.getDeliveryPrice())
                .servicePrice(cart.getServicePrice())
                .totalPrice(cart.getTotalPrice())
                .build();
    }

    @Transactional
    public void clearCart(Long cartId) {
        repository.updateStatus(cartId, CartStatus.CANCELLED);
    }

    public CartDTO increaseCartTotalPrice(Cart cart, BigDecimal totalPrice) {
        cart.setTotalPrice(cart.getTotalPrice().add(totalPrice));
        repository.save(cart);
        return buildCartDTO(cart);
    }
}
