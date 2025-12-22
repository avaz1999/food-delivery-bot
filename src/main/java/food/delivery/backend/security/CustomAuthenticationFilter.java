package food.delivery.backend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import food.delivery.backend.enums.Role;
import food.delivery.backend.exception.BadRequestException;
import food.delivery.backend.exception.ResponseCodes;
import food.delivery.backend.model.dto.AuthPrincipalDTO;
import food.delivery.backend.model.request.SignInRequest;
import food.delivery.backend.model.response.GenericResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;


@Slf4j
@Setter
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager,
                                      JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        super.setFilterProcessesUrl("/api/sign-in");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        try {
            SignInRequest dto = new ObjectMapper().readValue(request.getReader(), SignInRequest.class);
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());
            return authenticationManager.authenticate(token);
        }catch (Exception e) {
            log.error("METHOD = attemptAuthentication, MESSAGE = {}", e.getMessage());
            throw new BadRequestException(ResponseCodes.LOGIN_FAILED, e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication auth) throws IOException {

        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();

        var principal = AuthPrincipalDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .build();

        Role role = user.getRole();

        String accessToken = jwtService.generateAccessToken(principal, role.name(), request.getRequestURL().toString());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(),
                new GenericResponse<>("access_token", accessToken));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {
        GenericResponse<String> error = GenericResponse.fail(ResponseCodes.USERNAME_OR_PASSWORD_INCORRECT);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), error);
    }

}
