package food.delivery.backend.service.impl;


import food.delivery.backend.entity.User;
import food.delivery.backend.enums.Status;
import food.delivery.backend.exception.ResponseCodes;
import food.delivery.backend.repository.UserRepository;
import food.delivery.backend.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created by Avaz Absamatov
 * Date: 11/13/2025
 */
@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameAndStatus(username, Status.ACTIVE)
                .orElseThrow(() -> new UsernameNotFoundException(ResponseCodes.USER_NOT_FOUND.getMessage()));

        return CustomUserDetails.builder()
                .id(user.getId())
                .password(user.getPassword())
                .role(user.getRole())
                .username(user.getUsername())
                .build();
    }

}
