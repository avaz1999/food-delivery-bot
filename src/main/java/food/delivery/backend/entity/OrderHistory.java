package food.delivery.backend.entity;

import food.delivery.backend.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

/**
 * Created by Avaz Absamatov
 * Date: 1/13/2026
 */
@Entity
@Table(
        name = "order_history",
        indexes = {
                @Index(name = "idx_order_history_order", columnList = "order_id"),
                @Index(name = "idx_order_history_status", columnList = "status")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderHistory extends GenericEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status =  OrderStatus.NEW;
}
