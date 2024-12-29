package org.example.server.exception;

public class InvalidTagTaskAssociationException extends RuntimeException {
    public InvalidTagTaskAssociationException(String message) {
        super(message);
    }
}
