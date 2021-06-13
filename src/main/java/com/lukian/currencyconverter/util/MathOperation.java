package com.lukian.currencyconverter.util;

import com.lukian.currencyconverter.exception.BelowZeroException;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.lukian.currencyconverter.constant.Constant.AFTER_COMMISSION;

public class MathOperation {

    private MathOperation() {
    }

    public static BigDecimal divide(BigDecimal value, BigDecimal ask) throws BelowZeroException {
        validate(value);
        validateZero(ask);
        return commission(value).divide(ask, 2, RoundingMode.HALF_EVEN);
    }

    public static BigDecimal multiply(BigDecimal value, BigDecimal bid) throws BelowZeroException {
        validate(value);
        validate(bid);
        return commission(value)
                .multiply(bid).setScale(2, RoundingMode.HALF_EVEN);
    }

    private static void validate(BigDecimal value) throws BelowZeroException {
        if (value.compareTo(BigDecimal.ZERO) < 0) throw new BelowZeroException(String.format("value %s below zero", value));
    }

    private static void validateZero(BigDecimal value) throws NullPointerException, BelowZeroException {
        validate(value);
        if (value.compareTo(BigDecimal.ZERO) == 0) throw new NullPointerException("value is zero");
    }

    private static BigDecimal commission(BigDecimal value) {
        return value.multiply(new BigDecimal(AFTER_COMMISSION));
    }
}
