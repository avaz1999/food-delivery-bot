package food.delivery.bot.service.base.impl;

import food.delivery.backend.enums.Language;
import food.delivery.backend.model.dto.CartDTO;
import food.delivery.backend.model.dto.CartItemDTO;
import food.delivery.backend.model.dto.OrderDTO;
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
                .append(BotMessages.DELIVERY_TEXT.getMessageWPar(
                        language,
                        cartDTO.getDeliveryPrice() != null ? cartDTO.getDeliveryPrice() : "0"));

        template.append("\n")
                .append(BotMessages.SERVICE_TEXT.getMessageWPar(
                        language,
                        cartDTO.getServicePrice() != null ? cartDTO.getServicePrice() : "0"));

        template.append("\n")
                .append(BotMessages.TOTAL_TEXT.getMessageWPar(
                        language,
                        cartDTO.getTotalPrice() != null ? cartDTO.getTotalPrice() : "0"));
        return template.toString();
    }

    @Override
    public String orderTemplate(Language language, OrderDTO order) {
        StringBuilder sb = new StringBuilder();
        sb.append(BotMessages.ORDER_TEMPLATE1.getMessage(language));

        sb.append(BotMessages.ORDER_TEMPLATE2.getMessage(language))
                .append(order.getOrderId())
                .append("*\n");

        sb.append(BotMessages.ORDER_TEMPLATE3.getMessage(language))
                .append(order.getStatus())
                .append("*\n\n");

        sb.append(BotMessages.ORDER_TEMPLATE4.getMessage(language))
                .append(order.getAddress())
                .append("\n\n");

        sb.append(BotMessages.ORDER_TEMPLATE5.getMessage(language));

        for (CartItemDTO item : order.getItems()) {
            sb.append(item.getQuantity())
                    .append("️⃣ ✖️ ")
                    .append(item.getItemName())
                    .append("\n");
        }

        sb.append(BotMessages.ORDER_TEMPLATE6.getMessage(language))
                .append(order.getPaymentType())
                .append("*\n\n");

        sb.append("──────────────\n");
        sb.append(BotMessages.ORDER_TEMPLATE7.getMessage(language));

        sb.append(BotMessages.ORDER_TEMPLATE8.getMessage(language))
                .append(order.getItemPrice())
                .append(BotMessages.ORDER_TEMPLATE9.getMessage(language));

        sb.append(BotMessages.ORDER_TEMPLATE10.getMessage(language))
                .append(order.getDeliveryPrice())
                .append(BotMessages.ORDER_TEMPLATE9.getMessage(language));

        if (order.getServicePrice() != null) {
            sb.append(BotMessages.ORDER_TEMPLATE11.getMessage(language))
                    .append(order.getServicePrice())
                    .append(BotMessages.ORDER_TEMPLATE9.getMessage(language));
        }

        sb.append(BotMessages.ORDER_TEMPLATE12.getMessage(language))
                .append(order.getTotalPrice())
                .append(BotMessages.ORDER_TEMPLATE9.getMessage(language));

        sb.append("──────────────\n\n");
        sb.append(BotMessages.ORDER_TEMPLATE13.getMessage(language));

        return sb.toString();
    }

    private static String toEmoji(Integer num) {
        if (num == null) return "0️⃣";

        String[] emojis = {
                "0️⃣", "1️⃣", "2️⃣", "3️⃣", "4️⃣",
                "5️⃣", "6️⃣", "7️⃣", "8️⃣", "9️⃣"
        };

        StringBuilder result = new StringBuilder();
        for (char c : num.toString().toCharArray()) {
            if (Character.isDigit(c)) {
                result.append(emojis[c - '0']);
            }
        }
        return result.toString();
    }

}
