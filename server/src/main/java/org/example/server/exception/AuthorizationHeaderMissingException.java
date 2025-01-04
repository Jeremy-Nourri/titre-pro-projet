package org.example.server.exception;

public class AuthorizationHeaderMissingException extends RuntimeException{
    public AuthorizationHeaderMissingException(String message) {
        super(message);
    }
}
