package food.delivery.backend.repository;

import food.delivery.backend.entity.Category;
import food.delivery.backend.enums.FoodStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Created by Avaz Absamatov
 * Date: 12/21/2025
 */
public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {
    boolean existsByNameUz(String nameUz);

    boolean existsByNameRu(String nameRu);

    List<Category> findAllByStatusAndParent_Id(FoodStatus status, Long parentId);
}
