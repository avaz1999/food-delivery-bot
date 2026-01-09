package food.delivery.backend.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Avaz Absamatov
 * Date: 1/6/2026
 */
@Entity
@Table(name = "unified_tariffs")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnifiedTariff  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // DELIVERY
    @Column(nullable = false)
    private BigDecimal pricePerKm = BigDecimal.valueOf(5000);

    @Column(nullable = false)
    private Integer freeKm = 3;

    @Column(nullable = false)
    private Integer fullyFreeThresholdKm = 3;

    @Column(nullable = false)
    private boolean freeDelivery = false;

    @Column(nullable = false)
    private BigDecimal servicePrice = BigDecimal.ZERO;

    @Column(nullable = false)
    private boolean freeService = false;

    @Column(nullable = false)
    private boolean active = true;

    private String name;

}
