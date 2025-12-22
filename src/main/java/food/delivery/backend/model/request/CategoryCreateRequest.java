package food.delivery.backend.model.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Created by Avaz Absamatov
 * Date: 12/21/2025
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryCreateRequest {

    @NotBlank(message = "{name.blank.message}")
    @Size(min = 2, max = 100, message = "name.size.message")
    @Pattern(regexp = "^[\\p{L}\\p{M}\\s\\d\\-.,()]+$",
            message = "name.pattern.message")
    private String nameUz;

    @NotBlank(message = "{name.blank.message}")
    @Size(min = 2, max = 100, message = "name.size.message")
    @Pattern(regexp = "^[\\p{L}\\p{M}\\s\\d\\-.,()]+$",
            message = "name.pattern.message")
    private String nameRu;

    @NotBlank(message = "{description.blank.message}")
    @Size(min = 2, max = 100, message = "description.size.message")
    @Pattern(regexp = "^[\\p{L}\\p{M}\\s\\d\\-.,()]+$",
            message = "description.pattern.message")
    private String descriptionUz;

    @NotBlank(message = "{description.blank.message}")
    @Size(min = 2, max = 100, message = "description.size.message")
    @Pattern(regexp = "^[\\p{L}\\p{M}\\s\\d\\-.,()]+$",
            message = "description.pattern.message")
    private String descriptionRu;

    @Min(value = 1, message = "{parentId.min.value.message}")
    private Long parentId;
}
