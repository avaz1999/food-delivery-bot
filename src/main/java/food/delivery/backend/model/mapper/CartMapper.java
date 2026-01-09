package food.delivery.backend.model.mapper;

import food.delivery.backend.entity.Cart;
import food.delivery.backend.model.dto.CartDTO;
import food.delivery.backend.model.dto.CartItemDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Avaz Absamatov
 * Date: 1/6/2026
 */
@Component
@RequiredArgsConstructor
public class CartMapper {

    private final CartItemMapper cartItemMapper;

    public CartDTO toDTO(Cart cart) {

        List<CartItemDTO> itemList =
                cartItemMapper.toDTOList(cart.getItems());

        return CartDTO.builder()
                .id(cart.getId())
                .items(itemList)
                .totalPrice(cart.getTotalPrice())
                .build();
    }
}
