package food.delivery.backend.repository;

import food.delivery.backend.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Created by Avaz Absamatov
 * Date: 1/6/2026
 */
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Optional<Restaurant> findByMainBranchTrueAndActiveTrue();

    List<Restaurant> findAllByActiveTrue();
}
