package com.lukian.currencyconverter.util;

import com.lukian.currencyconverter.exception.BelowZeroException;
import com.lukian.currencyconverter.model.Code;

import java.math.BigDecimal;

import static com.lukian.currencyconverter.constant.Constant.AFTER_COMMISSION;

public class Utility {

    private Utility() {
    }

    public static boolean isIdentical(Code code, Code targetCode) {
        return code == targetCode;
    }

    public static void validate(BigDecimal value) throws BelowZeroException {
        if (value.compareTo(BigDecimal.ZERO) < 0) throw new BelowZeroException(String.format("value %s below zero", value));
    }

    public static BigDecimal commission(BigDecimal value) {
        return value.multiply(new BigDecimal(AFTER_COMMISSION));
    }
}
