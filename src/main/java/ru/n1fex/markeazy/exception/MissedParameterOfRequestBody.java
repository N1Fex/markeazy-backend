package ru.n1fex.markeazy.exception;

public class MissedParameterOfRequestBody extends RuntimeException {
    public MissedParameterOfRequestBody(String message) {
        super(message);
    }
}
