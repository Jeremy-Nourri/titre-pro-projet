package org.example.server.exception;

public class UnauthorizedProjectAccessException extends RuntimeException {
    public UnauthorizedProjectAccessException(String message) {
        super(message);
    }
}