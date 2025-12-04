package food.delivery.backend.dto.request;

import food.delivery.backend.enums.Language;
import food.delivery.backend.enums.Role;
import food.delivery.backend.enums.State;
import food.delivery.backend.enums.Status;
import lombok.*;

/**
 * Created by Avaz Absamatov
 * Date: 12/2/2025
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BotUserDTO {
    private String fullName;
    private Long chatId;
    private Long userId;
    private Language language;
    private State state;
    private Status status;
    private Role role;
}
