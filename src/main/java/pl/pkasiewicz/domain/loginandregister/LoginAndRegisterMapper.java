package pl.pkasiewicz.domain.loginandregister;

import pl.pkasiewicz.domain.loginandregister.dto.RegisterUserDto;
import pl.pkasiewicz.domain.loginandregister.dto.RegistrationResultDto;
import pl.pkasiewicz.domain.loginandregister.dto.UserDto;

class LoginAndRegisterMapper {

    static User mapToEntity(RegisterUserDto registerUserDto) {
        return User.builder()
                        .username(registerUserDto.username())
                        .password(registerUserDto.password())
                        .build();
    }

    static RegistrationResultDto mapToRegistrationResultDto(User user) {
        return RegistrationResultDto.builder()
                .id(user.id())
                .created(true)
                .username(user.username())
                .build();
    }

    static UserDto mapToUserDto(User user) {
        return UserDto.builder()
                .id(user.id())
                .username(user.username())
                .password(user.password())
                .build();
    }
}
