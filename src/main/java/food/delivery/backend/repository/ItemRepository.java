package food.delivery.backend.repository;

import food.delivery.backend.entity.Item;
import food.delivery.backend.enums.FoodStatus;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Avaz Absamatov
 * Date: 12/27/2025
 */
public interface ItemRepository extends JpaRepository<Item, Long> {
    boolean existsByNameUz(String nameUz);

    boolean existsByNameRu(String nameUz);

    Item findByCategory_IdAndStatus(Long categoryId, FoodStatus status);

    Item findByIdAndStatus(Long itemId, FoodStatus foodStatus);
}
