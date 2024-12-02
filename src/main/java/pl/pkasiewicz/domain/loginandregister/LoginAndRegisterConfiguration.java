package pl.pkasiewicz.domain.loginandregister;

public class LoginAndRegisterConfiguration {

    LoginAndRegisterFacade createForTest(LoginRepository loginRepository) {
        return new LoginAndRegisterFacade(loginRepository);
    }
}
