package food.delivery.backend.entity;

import food.delivery.backend.enums.Role;
import food.delivery.backend.enums.Status;
import jakarta.persistence.*;
import lombok.*;

/**
 * Created by Avaz Absamatov
 * Date: 11/12/2025
 */
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User extends GenericEntity {
    @Column(name = "first_name", length = 100, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 100, nullable = false)
    private String lastName;

    @Column(name = "username", length = 100, nullable = false, unique = true)
    private String username;

    @Column(name = "password", length = 500, nullable = false)
    private String password;

    @Column(name = "phone", length = 12)
    private String phone;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "dev")
    private boolean isDeveloper = false;

    @Enumerated(EnumType.STRING)
    private Role role;
}
