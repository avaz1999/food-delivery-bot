package food.delivery.backend.entity;

import food.delivery.backend.enums.FoodStatus;
import jakarta.persistence.*;
import lombok.*;

/**
 * Created by Avaz Absamatov
 * Date: 12/21/2025
 */
@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category extends GenericEntity {

    @Column(name = "name_uz", nullable = false, unique = true)
    private String nameUz;

    @Column(name = "name_ru", nullable = false, unique = true)
    private String nameRu;

    @Column(name = "description_uz")
    private String descriptionUz;

    @Column(name = "description_ru")
    private String descriptionRu;

    @Enumerated(EnumType.STRING)
    private FoodStatus status;

    @ManyToOne
    private Category parent;
}
