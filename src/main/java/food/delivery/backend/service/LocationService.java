package food.delivery.backend.service;

/**
 * Created by Avaz Absamatov
 * Date: 12/15/2025
 */
public interface LocationService {
    String getAddressByLongitudeAndLatitude(double longitude, double latitude);
}
