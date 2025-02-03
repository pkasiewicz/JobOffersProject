package pl.pkasiewicz.infrastructure.loginandregister.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.pkasiewicz.domain.loginandregister.LoginAndRegisterFacade;
import pl.pkasiewicz.domain.loginandregister.dto.RegisterUserDto;
import pl.pkasiewicz.domain.loginandregister.dto.RegistrationResultDto;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class RegisterController {

    private final LoginAndRegisterFacade loginAndRegisterFacade;
    private final PasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/register")
    public ResponseEntity<RegistrationResultDto> registerUser(@Valid @RequestBody RegisterUserDto registerUserDto) {
        String encodedPassword = bCryptPasswordEncoder.encode(registerUserDto.password());
        RegistrationResultDto registeredUser = loginAndRegisterFacade.register(
                new RegisterUserDto(registerUserDto.username(), encodedPassword)
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
    }
}
