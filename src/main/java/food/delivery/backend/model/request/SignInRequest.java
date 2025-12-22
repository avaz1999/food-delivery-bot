package food.delivery.backend.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * Created by Avaz Absamatov
 * Date: 12/22/2025
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SignInRequest {
    @NotBlank(message = "{validation.username.not.blank}")
    private String username;

    @NotBlank(message = "{validation.password.not.blank}")
    @ToString.Exclude
    private String password;
}