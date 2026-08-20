package com.whackamole.exceptions;

//Checked exception for high score read/write failures.

public class HighScoreException extends Exception {
    public HighScoreException(String message) { super(message); }
    public HighScoreException(String message, Throwable cause) { super(message, cause); }
}
