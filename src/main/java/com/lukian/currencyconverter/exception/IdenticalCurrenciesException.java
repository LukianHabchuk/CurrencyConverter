package com.lukian.currencyconverter.exception;

public class IdenticalCurrenciesException extends RuntimeException {
    public IdenticalCurrenciesException(String message) {
        super(message);
    }
}
