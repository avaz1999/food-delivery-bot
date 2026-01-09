package food.delivery.backend.service;

import food.delivery.backend.entity.Cart;
import food.delivery.backend.repository.UnifiedTariffRepository;
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
public class UnifiedTariffService {
    private final UnifiedTariffRepository repository;

    public void attachTariffSnapshot(Cart cart, Double distanceKm, BigDecimal delivery, BigDecimal service) {

    }
}
