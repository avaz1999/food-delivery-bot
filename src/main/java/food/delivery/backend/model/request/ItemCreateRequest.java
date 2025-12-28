package food.delivery.backend.model.request;

import food.delivery.backend.enums.FoodStatus;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

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
public class ItemCreateRequest {

    /* ================= NAME ================= */

    @NotBlank(message = "{name.blank.message}")
    @Size(min = 2, max = 100, message = "{name.size.message}")
    @Pattern(
            regexp = "^[\\p{L}\\p{M}\\s\\d\\-.,()]+$",
            message = "{name.pattern.message}"
    )
    private String nameUz;

    @NotBlank(message = "{name.blank.message}")
    @Size(min = 2, max = 100, message = "{name.size.message}")
    @Pattern(
            regexp = "^[\\p{L}\\p{M}\\s\\d\\-.,()]+$",
            message = "{name.pattern.message}"
    )
    private String nameRu;

    /* ================= DESCRIPTION ================= */

    @NotBlank(message = "{description.blank.message}")
    @Size(min = 2, max = 500, message = "{description.size.message}")
    @Pattern(
            regexp = "^[\\p{L}\\p{M}\\s\\d\\-.,()]+$",
            message = "{description.pattern.message}"
    )
    private String descriptionUz;

    @NotBlank(message = "{description.blank.message}")
    @Size(min = 2, max = 500, message = "{description.size.message}")
    @Pattern(
            regexp = "^[\\p{L}\\p{M}\\s\\d\\-.,()]+$",
            message = "{description.pattern.message}"
    )
    private String descriptionRu;

    /* ================= PRICE ================= */

    @NotNull(message = "{price.null.message}")
    @DecimalMin(value = "0.01", message = "{price.min.message}")
    private BigDecimal price;

    @NotNull(message = "{price.null.message}")
    @DecimalMin(value = "0.00", message = "{discount.min.message}")
    private BigDecimal discountPrice;

    /* ================= STATUS ================= */

    @NotNull(message = "{status.null.message}")
    private FoodStatus status;

    /* ================= CATEGORY ================= */

    @NotNull(message = "{category.null.message}")
    @Positive(message = "{category.positive.message}")
    private Long categoryId;

    /* ================= IMAGE ================= */

    private MultipartFile image;
}