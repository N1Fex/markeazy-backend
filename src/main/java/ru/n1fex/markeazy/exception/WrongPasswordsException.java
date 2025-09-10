package ru.n1fex.markeazy.exception;

public class WrongPasswordsException extends RuntimeException {
    public WrongPasswordsException(String message) {
        super(message);
    }
}
