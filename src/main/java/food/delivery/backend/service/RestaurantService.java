package food.delivery.backend.service;

import food.delivery.backend.entity.Restaurant;
import food.delivery.backend.model.dto.LocationDTO;
import food.delivery.backend.repository.RestaurantRepository;
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
public class RestaurantService {
    private final RestaurantRepository repository;

    public LocationDTO getRestaurantLocation() {

        Restaurant rest =
                repository
                        .findByMainBranchTrueAndActiveTrue()
                        .orElseThrow(() ->
                                new IllegalStateException("Main restaurant location not configured"));

        return new LocationDTO(rest.getLatitude(), rest.getLongitude());
    }
}
