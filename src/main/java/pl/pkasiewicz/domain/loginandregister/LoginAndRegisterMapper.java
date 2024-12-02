package pl.pkasiewicz.domain.loginandregister;

import pl.pkasiewicz.domain.loginandregister.dto.RegisterUserDto;
import pl.pkasiewicz.domain.loginandregister.dto.RegistrationResultDto;
import pl.pkasiewicz.domain.loginandregister.dto.UserDto;

class LoginAndRegisterMapper {

    public static User mapToEntity(RegisterUserDto registerUserDto) {
        return User.builder()
                        .username(registerUserDto.username())
                        .password(registerUserDto.password())
                        .build();
    }

    public static RegistrationResultDto mapToRegistrationResultDto(User user) {
        return RegistrationResultDto.builder()
                .id(user.getId())
                .created(true)
                .username(user.getUsername())
                .build();
    }

    public static UserDto mapToUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .build();
    }
}
