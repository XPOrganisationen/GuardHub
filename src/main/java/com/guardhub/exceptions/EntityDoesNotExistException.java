package com.guardhub.exceptions;

public class EntityDoesNotExistException extends RuntimeException {
    public EntityDoesNotExistException(String message) {
        super(message);
    }
}
