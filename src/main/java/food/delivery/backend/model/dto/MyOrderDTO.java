package food.delivery.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Avaz Absamatov
 * Date: 1/15/2026
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyOrderDTO {
    private Long id;
    private String orderId;
    private String status;
    private List<CartItemDTO> items = new ArrayList<>();
}
