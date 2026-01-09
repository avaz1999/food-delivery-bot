package food.delivery.backend.repository;

import food.delivery.backend.entity.CartItem;
import org.hibernate.type.descriptor.converter.spi.JpaAttributeConverter;

/**
 * Created by Avaz Absamatov
 * Date: 1/6/2026
 */
public interface CartItemRepository extends JpaAttributeConverter<CartItem, String> {
}
