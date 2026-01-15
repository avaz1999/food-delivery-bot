package food.delivery.backend.service;

import food.delivery.backend.entity.*;
import food.delivery.backend.enums.CartStatus;
import food.delivery.backend.enums.Language;
import food.delivery.backend.enums.OrderStatus;
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
import java.util.ArrayList;
import java.util.List;

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
    private final CartItemService cartItemService;

    @Transactional
    public OrderDTO createOrder(BotUser botUser, PaymentType paymentType) {
        Cart cart = cartService.getActiveCartByUser(botUser.getId());

        Order order = this.buildOrder(cart, botUser, paymentType);
        orderRepository.save(order);

        OrderHistory orderHistory = this.buildOrderHistory(order);
        orderHistoryRepository.save(orderHistory);

        CartDTO cartDTO = cartService.buildCartDTO(cart);
        cart.setStatus(CartStatus.ORDERED);
        List<CartItem> list = new ArrayList<>();

        for (CartItem item : cart.getItems()) {
            item.setStatus(CartStatus.ORDERED);
            list.add(item);
        }

        cartService.saveCart(cart);
        cartItemService.save(list);
        return buildOrderDTO(order, cartDTO, botUser.getLanguage());
    }

    private Order buildOrder(Cart cart, BotUser botUser, PaymentType paymentType) {
        return Order.builder()
                .orderId(generateUniqueOrderId())
                .cart(cart)
                .phoneNumber(botUser.getPhone())
                .address(botUser.getAddress())
                .paymentType(paymentType)
                .status(OrderStatus.NEW)
                .totalPrice(cart.getTotalPrice())
                .build();
    }

    private OrderHistory buildOrderHistory(Order order) {
        OrderHistory orderHistory = OrderHistory.builder()
                .order(order)
                .status(OrderStatus.NEW)
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
