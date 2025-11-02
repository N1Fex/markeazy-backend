package ru.n1fex.markeazy.exception;

public class EmptyOrderCartException extends RuntimeException {
    public EmptyOrderCartException(String message) {
        super(message);
    }
}
