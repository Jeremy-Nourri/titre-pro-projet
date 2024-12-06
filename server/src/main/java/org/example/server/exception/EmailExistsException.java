package org.example.server.exception;

public class EmailExistsException extends RuntimeException{
    public EmailExistsException() {
        super("Email already exists");
    }
}
