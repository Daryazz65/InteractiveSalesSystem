package ru.cementpromo.exception;

public class IORuntimeException extends RuntimeException {
    public IORuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}