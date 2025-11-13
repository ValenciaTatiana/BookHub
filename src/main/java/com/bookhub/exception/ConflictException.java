package com.bookhub.exception;

/**
 * Excepción para representar conflictos de negocio como duplicados (HTTP 409).
 */
public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }

    public ConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}
