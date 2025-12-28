package food.delivery.backend.model.dto;

import food.delivery.backend.enums.FoodStatus;
import lombok.*;

import java.math.BigDecimal;

/**
 * Created by Avaz Absamatov
 * Date: 12/27/2025
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDTO {
    private Long id;

    private String name;

    private String description;

    private BigDecimal price;

    private BigDecimal discountPrice;

    private FoodStatus status;

    private byte[] image;

}
