package org.example.server.exception;

public class BoardColumnNotFoundException extends RuntimeException {
    public BoardColumnNotFoundException(String message) {
        super(message);
    }
}
