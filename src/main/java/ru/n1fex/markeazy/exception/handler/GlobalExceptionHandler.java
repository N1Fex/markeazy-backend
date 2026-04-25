package ru.n1fex.markeazy.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.n1fex.markeazy.exception.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseError handle(EmailNotFoundException exception) {
        log.error(exception.getMessage());
        return new ResponseError(HttpStatus.UNAUTHORIZED, exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseError handle(SellerNotFoundException exception) {
        log.error(exception.getMessage());
        return new ResponseError(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseError handle(EmailAlreadyExistsException exception) {
        log.error(exception.getMessage());
        return new ResponseError(HttpStatus.UNAUTHORIZED, exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseError handle(LoginAlreadyExistsException exception) {
        log.error(exception.getMessage());
        return new ResponseError(HttpStatus.UNAUTHORIZED, exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError handle(WrongOldPasswordException exception) {
        log.error(exception.getMessage());
        return new ResponseError(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseError handle(AuthenticationException exception) {
        String message = exception.getMessage().equals("Bad credentials") ?
                "Неправильная почта или пароль" :
                exception.getMessage();
        log.error(message);

        return new ResponseError(HttpStatus.UNAUTHORIZED, message);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError handle(WrongPasswordsException exception) {
        log.error(exception.getMessage());
        return new ResponseError(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseError handle(OrderProductAmountExceedsInStockException exception) {
        log.error(exception.getMessage(), exception);
        return new ResponseError(HttpStatus.CONFLICT, exception.getMessage());
    }
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError handle(EmptyOrderCartException exception) {
        log.error(exception.getMessage());
        return new ResponseError(HttpStatus.BAD_REQUEST, exception.getMessage());
    }



    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseError handle(ReviewNotFoundException exception) {
        log.error(exception.getMessage(), exception);
        return new ResponseError(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseError handle(Exception exception) {
        log.error(exception.getMessage(), exception);
        return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseError handle(AccessDeniedException exception) {
        log.error(exception.getMessage(), exception);
        return new ResponseError(HttpStatus.FORBIDDEN, exception.getMessage());
    }

}
