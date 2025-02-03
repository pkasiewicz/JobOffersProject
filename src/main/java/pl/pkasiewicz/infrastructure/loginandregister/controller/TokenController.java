package pl.pkasiewicz.infrastructure.loginandregister.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.pkasiewicz.infrastructure.security.jwt.JwtAuthenticationFacade;
import pl.pkasiewicz.infrastructure.loginandregister.controller.dto.JwtResponseDto;
import pl.pkasiewicz.infrastructure.loginandregister.controller.dto.TokenRequestDto;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class TokenController {

    private final JwtAuthenticationFacade jwtAuthenticationFacade;

    @PostMapping("/token")
    public ResponseEntity<JwtResponseDto> authenticateAndGenerateToken(@Valid @RequestBody TokenRequestDto tokenRequest) {
        final JwtResponseDto jwtResponse = jwtAuthenticationFacade.authenticateAndGenerateToken(tokenRequest);
        return ResponseEntity.ok(jwtResponse);
    }
}
