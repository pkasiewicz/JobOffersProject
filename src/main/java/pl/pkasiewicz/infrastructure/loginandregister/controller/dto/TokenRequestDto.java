package pl.pkasiewicz.infrastructure.loginandregister.controller.dto;

public record TokenRequestDto(
        String username,
        String password
) {
}
