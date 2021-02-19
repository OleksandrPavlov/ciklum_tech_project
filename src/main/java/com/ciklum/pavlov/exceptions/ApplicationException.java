package com.ciklum.pavlov.exceptions;

public class ApplicationException extends RuntimeException {
    public ApplicationException(String message) {
        super(message);
    }
    public ApplicationException(){}
}
