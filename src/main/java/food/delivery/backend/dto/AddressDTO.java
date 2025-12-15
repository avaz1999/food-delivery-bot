package food.delivery.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Avaz Absamatov
 * Date: 12/14/2025
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {
    private String neighborhood;
    private String county;
    private String city;
}
