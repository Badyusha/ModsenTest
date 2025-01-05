package com.modsen.booktrackerservice.exceptions;

public class BookIsNullException extends RuntimeException {
    public BookIsNullException(String message) {
        super(message);
    }
}
