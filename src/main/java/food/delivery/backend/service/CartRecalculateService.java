package food.delivery.backend.service;

import food.delivery.backend.entity.BotUser;
import food.delivery.backend.entity.Cart;
import food.delivery.backend.entity.CartItem;
import food.delivery.backend.entity.UnifiedTariff;
import food.delivery.backend.model.dto.CartDTO;
import food.delivery.backend.model.dto.LocationDTO;
import food.delivery.backend.model.mapper.CartItemMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Created by Avaz Absamatov
 * Date: 1/6/2026
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CartRecalculateService {
    private final UnifiedTariffService tariffService;
    private final DistanceService distanceService;
    private final RestaurantService restaurantService;
    private final LocationService locationService;
    private final CartItemMapper cartItemMapper;

    public CartDTO recalculateCart(Cart cart, BotUser botUser) {

        //1. Sum items
        BigDecimal itemsSum = cart.getItems()
                .stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 2. Calculate distance
        double distanceKm = 0.0;

        LocationDTO userLocation = locationService.getLocationByBotUser(botUser.getId());
        if (userLocation != null) {
            LocationDTO restaurantLocation = restaurantService.getRestaurantLocation();

            distanceKm = distanceService.distance(restaurantLocation, userLocation);
        }

        // 3. DELIVERY TARIFF
        BigDecimal delivery = calculateDelivery(distanceKm, cart.getUnifiedTariff());

        // 4. SERVICE TARIFF
        BigDecimal service = calculateService(cart.getUnifiedTariff());

        // 5. FINAL
        BigDecimal finalTotal =
                itemsSum.add(delivery).add(service);

        cart.setTotalPrice(finalTotal);

        tariffService.attachTariffSnapshot(cart, distanceKm, delivery, service);

        return CartDTO.builder()
                .id(cart.getId())
                .items(cartItemMapper.toDTOList(cart.getItems()))
                .itemsPrice(itemsSum)
                .deliveryPrice(delivery)
                .servicePrice(service)
                .totalPrice(finalTotal)
                .build();
    }

    private BigDecimal calculateDelivery(Double distanceKm, UnifiedTariff t) {

        if (t == null || t.isFreeDelivery())
            return BigDecimal.ZERO;

        long dist = (long) Math.ceil(distanceKm);

        if (dist <= t.getFullyFreeThresholdKm())
            return BigDecimal.ZERO;

        long paidKm = Math.max(0, dist - t.getFreeKm());

        return t.getPricePerKm()
                .multiply(BigDecimal.valueOf(paidKm));
    }
    private BigDecimal calculateService(UnifiedTariff t) {

        if (t == null || t.isFreeService())
            return BigDecimal.ZERO;

        return t.getServicePrice();
    }

}
