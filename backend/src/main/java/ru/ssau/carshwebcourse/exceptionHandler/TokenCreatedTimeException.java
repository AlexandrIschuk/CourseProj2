package ru.ssau.carshwebcourse.exceptionHandler;

public class TokenCreatedTimeException extends RuntimeException {
    public TokenCreatedTimeException(String message) {
        super(message);
    }
}
