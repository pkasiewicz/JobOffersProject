package pl.pkasiewicz.domain.loginandregister;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import pl.pkasiewicz.domain.loginandregister.dto.RegisterUserDto;
import pl.pkasiewicz.domain.loginandregister.dto.RegistrationResultDto;
import pl.pkasiewicz.domain.loginandregister.dto.UserDto;

@AllArgsConstructor
public class LoginAndRegisterFacade {

    private static final String USER_NOT_FOUND = "User not found";
    private final LoginRepository loginRepository;

    public RegistrationResultDto register(RegisterUserDto registerUserDto) {
        User savedUser = loginRepository.save(LoginAndRegisterMapper
                .mapToEntity(registerUserDto));
        return LoginAndRegisterMapper.mapToRegistrationResultDto(savedUser);
    }

    public UserDto findByUsername(String username) {
        return loginRepository.findByUsername(username)
                .map(LoginAndRegisterMapper::mapToUserDto)
                .orElseThrow(() -> new BadCredentialsException(USER_NOT_FOUND));
    }
}
