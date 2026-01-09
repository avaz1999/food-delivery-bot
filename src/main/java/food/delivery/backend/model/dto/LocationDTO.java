package food.delivery.backend.model.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Avaz Absamatov
 * Date: 1/6/2026
 */
@Getter
@Setter
public class LocationDTO {
    private Double latitude;
    private Double longitude;

    public LocationDTO(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
