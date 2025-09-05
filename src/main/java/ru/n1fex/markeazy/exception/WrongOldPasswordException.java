package ru.n1fex.markeazy.exception;

public class WrongOldPasswordException extends RuntimeException {
    public WrongOldPasswordException(String message) {
        super(message);
    }
}
