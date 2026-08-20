package com.whackamole.exceptions;

//Unchecked runtime exception for impossible game state errors.

public class InvalidGameStateException extends RuntimeException {
    public InvalidGameStateException(String message) { super(message); }
    public InvalidGameStateException(String message, Throwable cause) { super(message, cause); }
}
