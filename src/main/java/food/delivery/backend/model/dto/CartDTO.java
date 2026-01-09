package food.delivery.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Avaz Absamatov
 * Date: 1/6/2026
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDTO {
    private Long id;

    private List<CartItemDTO> items;

    private BigDecimal itemsPrice = BigDecimal.ZERO;

    private BigDecimal deliveryPrice = BigDecimal.ZERO;

    private BigDecimal servicePrice = BigDecimal.ZERO;

    /**
     * FINAL = items + delivery + service
     */
    private BigDecimal totalPrice = BigDecimal.ZERO;

}
