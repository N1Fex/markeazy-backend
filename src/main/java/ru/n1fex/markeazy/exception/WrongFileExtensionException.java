package ru.n1fex.markeazy.exception;

public class WrongFileExtensionException extends RuntimeException {
    public WrongFileExtensionException(String message) {
        super(message);
    }
}
