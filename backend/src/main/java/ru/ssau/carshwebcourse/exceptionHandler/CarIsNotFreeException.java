package ru.ssau.carshwebcourse.exceptionHandler;

public class CarIsNotFreeException extends RuntimeException {
    public CarIsNotFreeException(String message) {
        super(message);
    }
}
