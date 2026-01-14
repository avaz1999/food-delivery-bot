package food.delivery.backend.entity;

import food.delivery.backend.enums.CartStatus;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Avaz Absamatov
 * Date: 1/6/2026
 */
@Entity
@Table(name = "carts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "created_by", updatable = false)
    private Long createdBy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CartStatus status;

    @OneToMany(
            mappedBy = "cart",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<CartItem> items = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unified_tariff_id")
    private UnifiedTariff unifiedTariff;

    @OneToOne(mappedBy = "cart")
    private Order order;

    @Column(name = "delivery_price")
    private BigDecimal deliveryPrice;

    @Column(name = "service_price")
    private BigDecimal servicePrice;

    @Column(nullable = false)
    private BigDecimal totalPrice = BigDecimal.ZERO;

}
