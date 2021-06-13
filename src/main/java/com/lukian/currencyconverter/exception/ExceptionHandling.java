package com.lukian.currencyconverter.exception;

import com.lukian.currencyconverter.model.Code;
import com.lukian.currencyconverter.model.HttpResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class ExceptionHandling implements ErrorController {

    @ExceptionHandler(RateNotFoundException.class)
    public ResponseEntity<HttpResponse> rateNotFoundException(RateNotFoundException e) {
        return createHttpResponse(NOT_FOUND, e.getMessage().toUpperCase());
    }

    @ExceptionHandler(IdenticalCurrenciesException.class)
    public ResponseEntity<HttpResponse> identicalCurrenciesException(IdenticalCurrenciesException e) {
        return createHttpResponse(NOT_ACCEPTABLE, e.getMessage().toUpperCase());
    }

    @ExceptionHandler(BelowZeroException.class)
    public ResponseEntity<HttpResponse> belowZeroException(BelowZeroException e) {
        return createHttpResponse(BAD_REQUEST, e.getMessage().toUpperCase());
    }

    @ExceptionHandler(SourceNotFoundException.class)
    public ResponseEntity<HttpResponse> sourceNotFoundException(SourceNotFoundException e) {
        return createHttpResponse(INTERNAL_SERVER_ERROR, e.getMessage().toUpperCase());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<HttpResponse> badCurrencyCodeException() {
        return createHttpResponse(BAD_REQUEST, String.format("SUCH CURRENCY CODE IS NOT EXISTS! " +
                "YOU CAN CHOOSE ONE FROM THE FOLLOWING: %s", Arrays.toString(Code.values())));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpResponse> internalServerErrorException(Exception e) {
        return createHttpResponse(INTERNAL_SERVER_ERROR, e.getMessage().toUpperCase());
    }

    private ResponseEntity<HttpResponse> createHttpResponse(HttpStatus httpStatus, String message) {
        var httpResponse = new HttpResponse(httpStatus.value(),
                httpStatus, httpStatus.getReasonPhrase().toUpperCase(),
                message.toUpperCase());
        return new ResponseEntity<>(httpResponse, httpStatus);
    }
}
