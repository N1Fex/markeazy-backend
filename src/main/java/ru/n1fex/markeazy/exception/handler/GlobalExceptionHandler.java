package ru.n1fex.markeazy.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseError handle(EmailAlreadyExistsException exception) {
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
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError handle(EmptyOrderCartException exception) {
        log.error(exception.getMessage());
        return new ResponseError(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseError handle(Exception exception) {
        log.error(exception.getMessage(), exception);
        return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
    }
}
