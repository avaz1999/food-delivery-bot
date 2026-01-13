package food.delivery.backend.model.mapper;

import food.delivery.backend.entity.CartItem;
import food.delivery.backend.enums.CartStatus;
import food.delivery.backend.model.dto.CartItemDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Created by Avaz Absamatov
 * Date: 1/6/2026
 */
@Component
@RequiredArgsConstructor
public class CartItemMapper {
    public List<CartItemDTO> toDTOList(List<CartItem> list) {
        if (list == null || list.isEmpty())
            return new ArrayList<>();

        return list.stream()
                .filter(item -> Objects.equals(item.getStatus(), CartStatus.ACTIVE))
                .sorted(Comparator.comparing(CartItem::getItemName))
                .map(this::toDTO)
                .toList();
    }

    public CartItemDTO toDTO(CartItem e) {

        if (e == null) return null;

        return CartItemDTO.builder()
                .id(e.getId())
                .itemName(e.getItemName())
                .price(e.getPrice())
                .quantity(e.getQuantity())
                .totalPrice(e.getTotalPrice())
                .build();
    }

}
