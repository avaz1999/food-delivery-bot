package food.delivery.backend.config;

import food.delivery.backend.exception.ResponseCodes;
import food.delivery.backend.model.response.GenericResponse;
import food.delivery.backend.security.CustomAuthenticationFilter;
import food.delivery.backend.security.CustomAuthorizationFilter;
import food.delivery.backend.security.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Created by Avaz Absamatov
 * Date: 11/13/2025
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomAuthorizationFilter authorizationFilter;
    private final JwtService jwtService;
    private final AuthenticationConfiguration authConfig;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        AuthenticationManager authManager = authConfig.getAuthenticationManager();

        // Login filter
        CustomAuthenticationFilter authFilter = new CustomAuthenticationFilter(authManager, jwtService);
        authFilter.setFilterProcessesUrl("/api/sign-in");

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/sign-in").permitAll()
                        .requestMatchers("/", "/actuator/health").permitAll()
                        .requestMatchers(
                                "/swagger-ui/**", "/swagger-ui.html",
                                "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )

                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((req, res, authEx) -> {
                            res.setContentType("application/json");
                            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            res.getWriter().write(
                                    GenericResponse.of(ResponseCodes.UNAUTHORIZED.getCode(), "Token required or invalid")
                                            .toJson()
                            );
                        })
                        .accessDeniedHandler((req, res, e) -> {
                            res.setContentType("application/json");
                            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            res.getWriter().write(
                                    GenericResponse.of(ResponseCodes.FORBIDDEN.getCode(), "Access denied")
                                            .toJson()
                            );
                        })
                )

                .addFilterAt(authFilter, UsernamePasswordAuthenticationFilter.class) // 1. Login
                .addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class) // 2. JWT + SecurityContext.addFilterBefore(permissionFilter, UsernamePasswordAuthenticationFilter.class) // 3. Permission (USER_CREATE?)
        ;

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("http://localhost:3000", "https://admin.humo.uz", "https://callcenter.humo.uz"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
