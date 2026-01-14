package food.delivery.backend.service;

import food.delivery.backend.entity.BotUser;
import food.delivery.backend.entity.Cart;
import food.delivery.backend.entity.CartItem;
import food.delivery.backend.enums.CartStatus;
import food.delivery.backend.model.dto.CartDTO;
import food.delivery.backend.model.dto.ItemDTO;
import food.delivery.backend.repository.CartItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Avaz Absamatov
 * Date: 1/6/2026
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CartItemService {
    private final ItemService itemService;
    private final CartService cartService;
    private final CartRecalculateService recalculateService;
    private final CartItemRepository repository;

    @Transactional
    public CartDTO collectItem(Long itemId, int quantity, BotUser botUser) {
        // 1. CART
        Cart cart =
                cartService.getOrCreateActiveCart(botUser.getId());

        // 2. ITEM
        ItemDTO itemDTO =
                itemService.getItemById(itemId, botUser.getLanguage());

        // 3. CART ITEM
        CartItem cartItem = repository
                .findByCartIdAndItemIdAndStatus(
                        cart.getId(),
                        itemId,
                        CartStatus.ACTIVE
                )
                .orElse(null);

        boolean isNew = false;
        if (cartItem == null) {
            cartItem = buildCartItem(cart, itemDTO, quantity);
            isNew = true;
        } else {
            int newQuantity = cartItem.getQuantity() + quantity;
            cartItem.setQuantity(newQuantity);
            cartItem.setTotalPrice(
                    cartItem.getPrice().multiply(BigDecimal.valueOf(newQuantity))
            );
            // ❗ PRIMARY
            cartItem.setCart(cart);
            repository.save(cartItem);
        }


        // 4. ADD TO LIST
        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            cart.setItems(new ArrayList<>());
        }
        if (isNew) {
            if (cart.getItems() == null) {
                cart.setItems(new ArrayList<>());
            }
            cart.getItems().add(cartItem);
        }

        // 6. RECALCULATE - all keys
        CartDTO cartDTO = recalculateService.recalculateCart(cart, botUser);

        // 7. SAVE
        cartService.save(cart);

        return cartDTO;

    }

    private CartItem buildCartItem(Cart cart, ItemDTO itemDTO, Integer quantity) {
        BigDecimal totalPrice = itemDTO.getPrice().multiply(BigDecimal.valueOf(quantity));
        return CartItem.builder()
                .itemId(itemDTO.getId())
                .cart(cart)
                .status(CartStatus.ACTIVE)
                .itemName(itemDTO.getName())
                .price(itemDTO.getPrice())
                .quantity(quantity)
                .totalPrice(totalPrice)
                .build();
    }

    @Transactional
    public void clearCartItem(Long cartId) {
        repository.updateStatusByCartId(cartId, CartStatus.CANCELLED);
    }

    @Transactional
    public CartDTO incrementCartItem(Long cartItemId) {
        return changeQuantity(cartItemId, 1);
    }


    @Transactional
    public CartDTO decrementCartItem(Long cartItemId) {
        return changeQuantity(cartItemId, -1);
    }


    @Transactional
    public CartDTO changeQuantity(Long cartItemId, int delta) {

        CartItem cartItem = repository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("CartItem not found"));

        int newQuantity = cartItem.getQuantity() + delta;

        if (newQuantity <= 0) {
            cartItem.setStatus(CartStatus.CANCELLED);
            cartItem.setQuantity(1);
            cartItem.setTotalPrice(BigDecimal.ZERO);
            cartItem.getCart().setStatus(CartStatus.CANCELLED);
        } else {
            cartItem.setQuantity(newQuantity);
            cartItem.setTotalPrice(
                    cartItem.getPrice().multiply(BigDecimal.valueOf(newQuantity))
            );
        }

        repository.save(cartItem);

        return cartService.saveCart(cartItem.getCart());
    }

}
