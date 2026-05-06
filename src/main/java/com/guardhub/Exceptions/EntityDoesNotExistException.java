package com.guardhub.Exceptions;

public class EntityDoesNotExistException extends RuntimeException {
    public EntityDoesNotExistException(String message) {
      super(message);
    }
}
