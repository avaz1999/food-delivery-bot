package food.delivery.bot.service.base.impl;

import food.delivery.backend.enums.Language;
import food.delivery.backend.model.dto.CartDTO;
import food.delivery.backend.model.dto.CartItemDTO;
import food.delivery.bot.service.base.TemplateBuilder;
import food.delivery.bot.utils.BotMessages;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Avaz Absamatov
 * Date: 1/7/2026
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TemplateBuilderImpl implements TemplateBuilder {
    @Override
    public String cartTemplate(Language language, CartDTO cartDTO) {
        StringBuilder template = new StringBuilder();

        template.append(BotMessages.CART_MESSAGE.getMessage(language));

        List<CartItemDTO> cartItems = cartDTO.getItems();
        for (CartItemDTO item : cartItems) {
            template.append(toEmoji(item.getQuantity()))
                    .append(" ✖️ ")
                    .append(item.getItemName())
                    .append("\n");
        }

        template.append("\n")
                .append(BotMessages.ITEM_TEXT.getMessageWPar(language, cartDTO.getItemsPrice()));

        template.append("\n")
                .append(BotMessages.DELIVERY_TEXT.getMessageWPar(language, cartDTO.getDeliveryPrice()));

        template.append("\n")
                .append(BotMessages.SERVICE_TEXT.getMessageWPar(language, cartDTO.getServicePrice()));

        template.append("\n")
                .append(BotMessages.TOTAL_TEXT.getMessageWPar(language, cartDTO.getTotalPrice()));
        return template.toString();
    }

    private static String toEmoji(Integer num) {
        if (num == null) num = 0;

        String[] emojis = {"0️⃣", "1️⃣", "2️⃣", "3️⃣", "4️⃣", "5️⃣",
                "6️⃣", "7️⃣", "8️⃣", "9️⃣", "🔟"};
        if (num >= 0 && num < emojis.length) {
            return emojis[num];
        }
        return num + "";
    }
}
