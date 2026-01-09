package food.delivery.bot.service.base;

import food.delivery.backend.enums.Language;
import food.delivery.backend.model.dto.CartDTO;

/**
 * Created by Avaz Absamatov
 * Date: 1/7/2026
 */
public interface TemplateBuilder {
    String cartTemplate(Language language, CartDTO cartDTO);
}
