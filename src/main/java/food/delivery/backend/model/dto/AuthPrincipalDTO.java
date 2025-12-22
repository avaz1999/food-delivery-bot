package food.delivery.backend.model.dto;

import food.delivery.backend.enums.Status;
import lombok.Builder;

/**
 * Created by Avaz Absamatov
 * Date: 12/22/2025
 */
@Builder
public record AuthPrincipalDTO(Long id, String username, Status status) {}
