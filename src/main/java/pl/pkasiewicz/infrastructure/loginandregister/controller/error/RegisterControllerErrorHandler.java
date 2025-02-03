package pl.pkasiewicz.infrastructure.loginandregister.controller.error;

import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.pkasiewicz.infrastructure.loginandregister.controller.RegisterController;

@ControllerAdvice(basePackageClasses = {RegisterController.class})
@Log4j2
public class RegisterControllerErrorHandler {

    @ExceptionHandler(DuplicateKeyException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    public RegisterErrorResponse handleUserDuplicationException(DuplicateKeyException exception) {
        final String message = "User already exists";
        log.error(exception.getMessage());
        return new RegisterErrorResponse(message, HttpStatus.CONFLICT);
    }
}
