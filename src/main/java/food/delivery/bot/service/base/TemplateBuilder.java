package food.delivery.bot.service.base;

import food.delivery.backend.enums.Language;
import food.delivery.backend.model.dto.CartDTO;
import food.delivery.backend.model.dto.MyOrderDTO;
import food.delivery.backend.model.dto.OrderDTO;

import java.util.List;

/**
 * Created by Avaz Absamatov
 * Date: 1/7/2026
 */
public interface TemplateBuilder {
    String cartTemplate(Language language, CartDTO cartDTO);

    String orderTemplate(Language language, OrderDTO order);

    String buildMyOrders(List<MyOrderDTO> myOrders, Language language);
}
