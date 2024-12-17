package org.example.server.exception;

public class InvalidProjectDateException extends RuntimeException {
    public InvalidProjectDateException(String message) {
        super(message);
    }
}
