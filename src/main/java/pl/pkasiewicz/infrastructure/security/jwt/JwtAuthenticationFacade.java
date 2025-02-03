package pl.pkasiewicz.infrastructure.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import pl.pkasiewicz.infrastructure.loginandregister.controller.dto.JwtResponseDto;
import pl.pkasiewicz.infrastructure.loginandregister.controller.dto.TokenRequestDto;

import java.time.*;

@AllArgsConstructor
@Component
public class JwtAuthenticationFacade {

    private final AuthenticationManager authenticationManager;
    private final JwtConfigurationProperties properties;
    private final Clock clock;

    public JwtResponseDto authenticateAndGenerateToken(TokenRequestDto tokenRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(tokenRequest.username(), tokenRequest.password())
        );
        User user = (User) authentication.getPrincipal();
        String token = createToken(user);
        String username = user.getUsername();
        return JwtResponseDto.builder()
                .username(username)
                .token(token)
                .build();
    }

    private String createToken(User user) {
        String secretKey = properties.secret();
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        Instant now = LocalDateTime.now(clock).toInstant(ZoneOffset.UTC);
        Instant expiresAt = now.plus(Duration.ofDays(properties.expirationDays()));
        String issuer = properties.issuer();
        return JWT.create()
                .withSubject(user.getUsername())
                .withIssuedAt(now)
                .withExpiresAt(expiresAt)
                .withIssuer(issuer)
                .sign(algorithm);
    }
}
