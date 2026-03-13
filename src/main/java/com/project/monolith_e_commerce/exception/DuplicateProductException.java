package com.project.monolith_e_commerce.exception;

public class DuplicateProductException extends RuntimeException {
    public DuplicateProductException(String message) {
        super(message);
    }
}
