package ru.n1fex.markeazy.exception;

public class SomethingWentWrongException extends RuntimeException {
    public SomethingWentWrongException(String message) {
        super(message);
    }
    public SomethingWentWrongException() {
      super("Что-то пошло не так");
    }
}
