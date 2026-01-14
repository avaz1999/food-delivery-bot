package food.delivery.backend.service;

import food.delivery.backend.entity.BotUser;
import food.delivery.backend.entity.Cart;
import food.delivery.backend.entity.Order;
import food.delivery.backend.entity.OrderHistory;
import food.delivery.backend.enums.Language;
import food.delivery.backend.enums.PaymentType;
import food.delivery.backend.model.dto.CartDTO;
import food.delivery.backend.model.dto.OrderDTO;
import food.delivery.backend.repository.OrderHistoryRepository;
import food.delivery.backend.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

/**
 * Created by Avaz Absamatov
 * Date: 1/13/2026
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final SecureRandom random = new SecureRandom();
    private final CartService cartService;
    private final OrderRepository orderRepository;
    private final OrderHistoryRepository orderHistoryRepository;

    @Transactional
    public OrderDTO createOrder(BotUser botUser, PaymentType paymentType) {
        Cart cart = cartService.getActiveCartByUser(botUser.getId());

        Order order = this.buildOrder(cart, botUser, paymentType);
        orderRepository.save(order);

        OrderHistory orderHistory = this.buildOrderHistory(order);
        orderHistoryRepository.save(orderHistory);

        CartDTO cartDTO = cartService.buildCartDTO(cart);
        return buildOrderDTO(order, cartDTO, botUser.getLanguage());
    }

    private Order buildOrder(Cart cart, BotUser botUser, PaymentType paymentType) {
        return Order.builder()
                .orderId(generateUniqueOrderId())
                .cart(cart)
                .phoneNumber(botUser.getPhone())
                .address(botUser.getAddress())
                .paymentType(paymentType)
                .totalPrice(cart.getTotalPrice())
                .build();
    }

    private OrderHistory buildOrderHistory(Order order) {
        OrderHistory orderHistory = OrderHistory.builder()
                .order(order)
                .build();
        orderHistory.setCreatedBy(order.getCreatedBy());
        orderHistory.setCreatedAt(LocalDateTime.now());
        return orderHistory;
    }


    private OrderDTO buildOrderDTO(Order order, CartDTO cartDTO, Language language) {
        return OrderDTO.builder()
                .id(order.getId())
                .orderId(order.getOrderId())
                .status(order.getStatus().getLabel(language.name()))
                .address(order.getAddress())
                .items(cartDTO.getItems())
                .paymentType(order.getPaymentType().getLabel(language.name()))
                .itemPrice(cartDTO.getItemsPrice())
                .deliveryPrice(cartDTO.getDeliveryPrice())
                .servicePrice(cartDTO.getServicePrice())
                .totalPrice(order.getTotalPrice())
                .build();

    }

    private String generateUniqueOrderId() {

        String orderId;
        do {
            orderId = String.format("%05d", random.nextInt(100000));
        } while (orderRepository.existsByOrderId(orderId));

        return orderId;
    }
}
