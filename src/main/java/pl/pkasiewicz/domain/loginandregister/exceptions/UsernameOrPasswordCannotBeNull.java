package pl.pkasiewicz.domain.loginandregister.exceptions;

public class UsernameOrPasswordCannotBeNull extends RuntimeException {
    public UsernameOrPasswordCannotBeNull(String message) {
        super(message);
    }
}
