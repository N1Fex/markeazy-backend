package ru.n1fex.markeazy.exception;

public class OrderProductAmountExceedsInStockException extends RuntimeException {
    public OrderProductAmountExceedsInStockException(String message) {
        super(message);
    }
}
