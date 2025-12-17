package food.delivery.backend.entity;

import food.delivery.backend.enums.Language;
import food.delivery.backend.enums.Role;
import food.delivery.backend.enums.State;
import food.delivery.backend.enums.Status;
import jakarta.persistence.*;
import lombok.*;

/**
 * Created by Avaz Absamatov
 * Date: 12/1/2025
 */
@Entity
@Table(name = "bot_users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BotUser extends GenericEntity {
    @Column(name = "full_name")
    private String fullName;

    @Column(name = "username")
    private String username;

    @Column(name = "phone")
    private String phone;

    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "address")
    private String address;

    @Column(name = "tem_address")
    private String temAddress;

    @Column(name = "user_id")
    private Long userId;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    @Enumerated(EnumType.STRING)
    private State state = State.STATE_START;

    @Enumerated(EnumType.STRING)
    private Language language = Language.UZ;

    @Enumerated(EnumType.STRING)
    private Role role = Role.CLIENT;

}