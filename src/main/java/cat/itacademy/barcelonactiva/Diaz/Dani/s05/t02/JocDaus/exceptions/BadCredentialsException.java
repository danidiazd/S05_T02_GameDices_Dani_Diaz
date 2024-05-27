package cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.exceptions;

import org.springframework.security.core.AuthenticationException;

public class BadCredentialsException extends AuthenticationException {
    public BadCredentialsException(String message) {
        super(message);
    }
}
