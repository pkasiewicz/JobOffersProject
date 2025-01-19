package pl.pkasiewicz.infrastructure.offer.controller.error;

import org.springframework.http.HttpStatus;

public record JobOfferErrorResponse(
        String message,
        HttpStatus status) {
}
