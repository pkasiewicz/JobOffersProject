package pl.pkasiewicz.infrastructure.loginandregister.controller.error;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.pkasiewicz.infrastructure.loginandregister.controller.TokenController;

@ControllerAdvice(basePackageClasses = {TokenController.class})
@Log4j2
public class TokenControllerErrorHandler {

    public static final String BAD_CREDENTIALS = "Bad Credentials";

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseBody
    public TokenErrorResponse handleBadCredentialsException() {
        return new TokenErrorResponse(BAD_CREDENTIALS, HttpStatus.UNAUTHORIZED);
    }
}
