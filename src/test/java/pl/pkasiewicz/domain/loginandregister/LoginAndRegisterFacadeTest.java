package pl.pkasiewicz.domain.loginandregister;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import pl.pkasiewicz.domain.loginandregister.dto.RegisterUserDto;
import pl.pkasiewicz.domain.loginandregister.dto.RegistrationResultDto;
import pl.pkasiewicz.domain.loginandregister.dto.UserDto;
import pl.pkasiewicz.domain.loginandregister.exceptions.UserAlreadyExistException;
import pl.pkasiewicz.domain.loginandregister.exceptions.UsernameNotFoundException;
import pl.pkasiewicz.domain.loginandregister.exceptions.UsernameOrPasswordCannotBeNull;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertAll;

class LoginAndRegisterFacadeTest {

    private final LoginRepository loginRepository = new InMemoryLoginRepository();
    private final LoginAndRegisterFacade loginAndRegisterFacade = new LoginAndRegisterConfiguration().createForTest(loginRepository);
    
    @Test
    void should_register_user() {
        //given
        RegisterUserDto registerUserDto = new RegisterUserDto("username", "password");
        //when
        RegistrationResultDto register = loginAndRegisterFacade.register(registerUserDto);
        //then
        assertAll(
                () -> assertThat(register.created()).isTrue(),
                () -> assertThat(register.username()).isEqualTo("username")
        );
    }

    @Test
    void should_throw_user_already_exists_when_register_user() {
        //given
        RegisterUserDto existingUser = new RegisterUserDto("username", "password");
        loginAndRegisterFacade.register(existingUser);
        RegisterUserDto registerUserDto = new RegisterUserDto("username", "password");
        //when
        Throwable thrown = catchThrowable(() -> loginAndRegisterFacade.register(registerUserDto));
        //then
        AssertionsForClassTypes.assertThat(thrown)
                .isInstanceOf(UserAlreadyExistException.class)
                .hasMessageContaining("User already exists");

    }

    @Test
    void should_throw_username_or_password_cannot_be_empty_when_register_user() {
        //given
        RegisterUserDto registerUserDto = new RegisterUserDto("", null);
        //when
        Throwable thrown = catchThrowable(() -> loginAndRegisterFacade.register(registerUserDto));
        //then
        AssertionsForClassTypes.assertThat(thrown)
                .isInstanceOf(UsernameOrPasswordCannotBeNull.class)
                .hasMessageContaining("Username or password cannot be null");
    }

    @Test
    void should_find_user_by_username() {
        //given
        RegisterUserDto registerUserDto = new RegisterUserDto("username", "password");
        RegistrationResultDto register = loginAndRegisterFacade.register(registerUserDto);
        //when
        UserDto user = loginAndRegisterFacade.findByUsername(register.username());
        //then
        assertThat(user).isEqualTo(new UserDto(register.id(), "username", "password"));
    }

    @Test
    void should_throw_exception_if_user_not_found() {
        //given
        String username = "user";
        //when
        Throwable thrown = catchThrowable(() -> loginAndRegisterFacade.findByUsername(username));
        //then
        AssertionsForClassTypes.assertThat(thrown)
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User not found");
    }
}