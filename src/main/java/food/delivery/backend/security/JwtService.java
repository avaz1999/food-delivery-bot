package food.delivery.backend.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import food.delivery.backend.model.dto.AuthPrincipalDTO;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by Avaz Absamatov
 * Date: 12/22/2025
 */
@Service
public class JwtService {
    private final JwtProperties jwtProps;
    private final JWTVerifier verifier;

    public JwtService(JwtProperties jwtProps) {
        this.jwtProps = jwtProps;
        this.verifier = JWT.require(jwtProps.getAlgorithm()).build();
    }

    public String generateAccessToken(AuthPrincipalDTO principal, String role, String issuer) {
        return JWT.create()
                .withSubject(String.valueOf(principal.id()))
                .withClaim("username", principal.username())
                .withClaim("role", role)
                .withIssuer(issuer)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtProps.getAccessTokenExpiryMs()))
                .sign(jwtProps.getAlgorithm());
    }

    public DecodedJWT verifyToken(String token) {
        return verifier.verify(token);
    }

}
