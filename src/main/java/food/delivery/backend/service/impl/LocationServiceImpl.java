package food.delivery.backend.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import food.delivery.backend.service.LocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Created by Avaz Absamatov
 * Date: 12/15/2025
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String getAddressByLongitudeAndLatitude(double latitude, double longitude) {

        try {
            String url = "https://nominatim.openstreetmap.org/reverse"
                    + "?lat=" + latitude
                    + "&lon=" + longitude
                    + "&format=json"
                    + "&zoom=18"
                    + "&accept-language=uz";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("User-Agent", "FoodDeliveryBot/1.0 (+https://t.me/food_delivery_angren_bot; avazabsamtov7@gmail.com)")
                    .header("Referer", "https://t.me/food_delivery_angren_bot")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                log.warn("Nominatim HTTP failed: {}", response.statusCode());
                return null;
            }

            if (response.body().contains("Unable to geocode")) {
                log.warn("Nominatim: Unable to geocode — User-Agent or coordinate fail");
                return null;
            }
            StringBuilder addressBuilder = new StringBuilder();
            JsonNode root = mapper.readTree(response.body());
            JsonNode address = root.path("address");

            if (address.isMissingNode()) {
                return null;
            }

            if (address.has("district")) {
                addressBuilder.append(address.get("district").asText()).append(" ");
            }
            if (address.has("locality")) {
                addressBuilder.append(address.get("locality").asText()).append(" ");
            }
            if (address.has("house_number")) {
                addressBuilder.append(address.get("house_number").asText()).append(" ");
            }
            if (address.has("neighbourhood")) {
                addressBuilder.append(address.get("neighbourhood").asText()).append(" ");
            }
            if (address.has("county")) {
                addressBuilder.append(address.get("county").asText()).append(" ");
            }
            if (address.has("city")) {
                addressBuilder.append(address.get("city").asText()).append(" ");
            }

            return addressBuilder.toString();
        } catch (Exception e) {
            log.warn("HTTP failed: {}", e.getMessage());
            return null;
        }
    }
}
