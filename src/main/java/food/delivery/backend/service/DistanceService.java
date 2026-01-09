package food.delivery.backend.service;

import food.delivery.backend.model.dto.LocationDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Created by Avaz Absamatov
 * Date: 1/6/2026
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DistanceService {
    private static final double EARTH_RADIUS = 6371;

    public double distance(LocationDTO rest, LocationDTO userL) {

        double lat1 = Math.toRadians(rest.getLatitude());
        double lon1 = Math.toRadians(rest.getLongitude());
        double lat2 = Math.toRadians(userL.getLongitude());
        double lon2 = Math.toRadians(userL.getLongitude());

        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        double hav =
                Math.pow(Math.sin(dLat / 2), 2)
                        + Math.cos(lat1)
                        * Math.cos(lat2)
                        * Math.pow(Math.sin(dLon / 2), 2);

        double c =
                2 * Math.atan2(Math.sqrt(hav), Math.sqrt(1 - hav));

        return EARTH_RADIUS * c;
    }
}
