package com.lukian.currencyconverter.exception;

public class BelowZeroException extends Exception {
    public BelowZeroException(String message) {
        super(message);
    }
}
