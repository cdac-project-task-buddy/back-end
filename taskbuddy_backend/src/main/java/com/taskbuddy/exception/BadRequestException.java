package com.taskbuddy.exception;
// checked
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
