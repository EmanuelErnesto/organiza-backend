package com.organiza.api.exception;

public class HttpBadRequestException extends RuntimeException {
    public HttpBadRequestException(String message) {
        super(message);
    }
}
