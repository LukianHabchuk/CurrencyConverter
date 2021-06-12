package com.lukian.currencyconverter.constant;

public class HttpConstant {

    private HttpConstant() {
    }

    public static final int BAD_REQUEST_CODE = 400;
    public static final int NOT_ACCEPTABLE_CODE = 406;
    public static final int SERVER_ERROR_CODE = 500;

    public static final String BAD_REQUEST_MESSAGE = "Bad request";
    public static final String NOT_ACCEPTABLE_MESSAGE = "Not acceptable";
    public static final String SERVER_ERROR_MESSAGE = "Internal server error";
}
