package food.delivery.backend.model.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Avaz Absamatov
 * Date: 1/13/2026
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {
    private Long id;
    private String orderId;
    private String status;
    private String address;

    private List<CartItemDTO> items = new ArrayList<>();

    private String paymentType;

    private BigDecimal itemPrice;
    private BigDecimal deliveryPrice;
    private BigDecimal servicePrice;
    private BigDecimal totalPrice;
}
