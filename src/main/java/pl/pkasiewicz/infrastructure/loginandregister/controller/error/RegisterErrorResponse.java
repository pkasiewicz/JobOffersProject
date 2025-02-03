package pl.pkasiewicz.infrastructure.loginandregister.controller.error;

import org.springframework.http.HttpStatus;

public record RegisterErrorResponse(
        String message,
        HttpStatus status
) {
}
