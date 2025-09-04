package ru.n1fex.markeazy.exception;

import lombok.Data;

import java.util.Date;

@Data
public class AppError {
    private int code;
    private String message;
    private Date timestamp;

    public AppError(int code, String message) {
        this.code = code;
        this.message = message;
        this.timestamp = new Date();
    }
}
