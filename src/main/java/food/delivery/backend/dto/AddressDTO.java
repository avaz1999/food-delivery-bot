package food.delivery.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("house_number")
    private String houseNumber;
    @JsonProperty("locality")
    private String locality;
    @JsonProperty("district")
    private String district;

    @JsonProperty("neighborhood")
    private String neighborhood;
    @JsonProperty("county")
    private String county;
    @JsonProperty("city")
    private String city;

    public AddressDTO(String neighborhood, String district, String city) {
        this.neighborhood = neighborhood;
        this.district = district;
        this.city = city;
    }
}
