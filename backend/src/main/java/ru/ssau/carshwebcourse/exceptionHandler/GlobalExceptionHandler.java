package ru.ssau.carshwebcourse.exceptionHandler;

import org.postgresql.util.PSQLException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> noSuchElementException(MethodArgumentNotValidException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка: " + e.getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.toSet()));
    }

    @ExceptionHandler(PSQLException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<String> noSuchElementException(PSQLException e){
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Ошибка: " + e.getMessage());
    }
    @ExceptionHandler(InvalidTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<String> invalidTokenException(InvalidTokenException e){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error: " + e.getMessage());
    }

    @ExceptionHandler(TokenCreatedTimeException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<String> tokenCreatedTimeException(TokenCreatedTimeException e){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error: " + e.getMessage());
    }

    @ExceptionHandler(ArrayIndexOutOfBoundsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<String> arrayIndexOutOfBoundException(ArrayIndexOutOfBoundsException e){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error: invalid token");
    }

    @ExceptionHandler(CarIsNotFreeException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<String> carIsNotFreeException(CarIsNotFreeException e){
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: " + e.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> illegalStateException(IllegalStateException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Illegal State Error: " + e.getMessage());
    }
}

