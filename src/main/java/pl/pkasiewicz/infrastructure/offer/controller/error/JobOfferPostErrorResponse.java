package pl.pkasiewicz.infrastructure.offer.controller.error;

import org.springframework.http.HttpStatus;

public record JobOfferPostErrorResponse(
        String message,
        HttpStatus status) {
}
