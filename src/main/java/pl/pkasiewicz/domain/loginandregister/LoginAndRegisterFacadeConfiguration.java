package pl.pkasiewicz.domain.loginandregister;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoginAndRegisterFacadeConfiguration {

    @Bean
    LoginAndRegisterFacade loginAndRegisterFacade(LoginRepository loginRepository) {
        return new LoginAndRegisterFacade(loginRepository);
    }
}
