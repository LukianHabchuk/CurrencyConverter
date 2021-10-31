package com.lukian.currencyconverter.exception;

public class BelowZeroException extends RuntimeException {
    public BelowZeroException(String message) {
        super(message);
    }
}
