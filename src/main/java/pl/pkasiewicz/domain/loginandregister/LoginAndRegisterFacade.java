package pl.pkasiewicz.domain.loginandregister;

import lombok.AllArgsConstructor;
import pl.pkasiewicz.domain.loginandregister.dto.RegisterUserDto;
import pl.pkasiewicz.domain.loginandregister.dto.RegistrationResultDto;
import pl.pkasiewicz.domain.loginandregister.dto.UserDto;
import pl.pkasiewicz.domain.loginandregister.exceptions.UserAlreadyExistException;
import pl.pkasiewicz.domain.loginandregister.exceptions.UsernameNotFoundException;
import pl.pkasiewicz.domain.loginandregister.exceptions.UsernameOrPasswordCannotBeNull;

@AllArgsConstructor
public class LoginAndRegisterFacade {

    private static final String USER_NOT_FOUND = "User not found";
    private static final String USER_ALREADY_EXISTS = "User already exists";
    private static final String USERNAME_OR_PASSWORD_CANNOT_BE_NULL = "Username or password cannot be null";

    private final LoginRepository loginRepository;

    public RegistrationResultDto register(RegisterUserDto registerUserDto) {
        if (loginRepository.findByUsername(registerUserDto.username()).isPresent())
            throw new UserAlreadyExistException(USER_ALREADY_EXISTS);
        if (registerUserDto.username() == null || registerUserDto.password() == null)
            throw new UsernameOrPasswordCannotBeNull(USERNAME_OR_PASSWORD_CANNOT_BE_NULL);
        User savedUser = loginRepository.save(LoginAndRegisterMapper
                .mapToEntity(registerUserDto));
        return LoginAndRegisterMapper.mapToRegistrationResultDto(savedUser);
    }

    public UserDto findByUsername(String username) {
        return loginRepository.findByUsername(username)
                .map(LoginAndRegisterMapper::mapToUserDto)
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND));
    }
}
