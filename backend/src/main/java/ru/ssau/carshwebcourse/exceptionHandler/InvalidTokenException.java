package ru.ssau.carshwebcourse.exceptionHandler;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message) {
        super(message);
    }
}
