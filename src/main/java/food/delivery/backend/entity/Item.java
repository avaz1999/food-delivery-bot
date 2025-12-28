package food.delivery.backend.entity;

import food.delivery.backend.enums.FoodStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Created by Avaz Absamatov
 * Date: 12/21/2025
 */
@Entity
@Table(name = "items", indexes = {
        @Index(name = "idx_name_uz", columnList = "name_uz"),
        @Index(name = "idx_category_id", columnList = "category_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item extends GenericEntity {
    @Column(name = "name_uz")
    private String nameUz;

    @Column(name = "name_ru")
    private String nameRu;

    @Column(name = "description_uz")
    private String descriptionUz;

    @Column(name = "description_ru")
    private String descriptionRu;

    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "discount_price", precision = 10, scale = 2)
    private BigDecimal discountPrice;

    @Enumerated(EnumType.STRING)
    private FoodStatus status;

    @OneToOne
    private FileItem image;

    @ManyToOne
    private Category category;
}
