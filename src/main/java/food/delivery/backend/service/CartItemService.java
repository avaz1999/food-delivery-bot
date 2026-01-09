package food.delivery.backend.service;

import food.delivery.backend.entity.BotUser;
import food.delivery.backend.entity.Cart;
import food.delivery.backend.entity.CartItem;
import food.delivery.backend.model.dto.CartDTO;
import food.delivery.backend.model.dto.ItemDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

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

    public CartDTO collectItem(Long itemId, int quantity, BotUser botUser) {
        // 1. CART
        Cart cart =
                cartService.getOrCreateActiveCart(botUser.getId());

        // 2. ITEM
        ItemDTO itemDTO =
                itemService.getItemById(itemId, botUser.getLanguage());

        // 3. CART ITEM
        CartItem cartItem =
                buildCartItem(itemDTO, quantity);

        // ❗ PRIMARY
        cartItem.setCart(cart);

        // 4. ADD TO LIST
        cart.getItems().add(cartItem);

        // 6. RECALCULATE - all keys
        CartDTO cartDTO = recalculateService.recalculateCart(cart, botUser);

        // 7. SAVE
        cartService.save(cart);

        return cartDTO;

    }

    private CartItem buildCartItem(ItemDTO itemDTO, Integer quantity) {
        BigDecimal totalPrice = itemDTO.getPrice().multiply(BigDecimal.valueOf(quantity));
        return CartItem.builder()
                .itemId(itemDTO.getId())
                .itemName(itemDTO.getName())
                .price(itemDTO.getPrice())
                .quantity(quantity)
                .totalPrice(totalPrice)
                .build();
    }
}
