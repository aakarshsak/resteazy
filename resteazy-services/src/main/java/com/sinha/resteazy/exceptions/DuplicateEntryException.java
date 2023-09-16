package com.sinha.resteazy.exceptions;

public class DuplicateEntryException extends RuntimeException {
    public DuplicateEntryException(String message) {
        super(message + " already exist...");
    }
}
