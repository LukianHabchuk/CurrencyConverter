package com.lukian.currencyconverter.util;

import com.lukian.currencyconverter.exception.BelowZeroException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.lukian.currencyconverter.constant.Constant.AFTER_COMMISSION;
import static com.lukian.currencyconverter.util.MathOperation.*;
import static org.junit.jupiter.api.Assertions.*;

class MathOperationTest {

    private final BigDecimal VALUE100 = new BigDecimal(100);
    private final BigDecimal VALUE_MINUS100 = new BigDecimal(-100);
    private final BigDecimal VALUE0 = new BigDecimal(0);

    @Test
    void shouldMultiply() throws BelowZeroException {
        assertEquals(VALUE100.multiply(new BigDecimal(AFTER_COMMISSION))
                .multiply(VALUE100).setScale(2, RoundingMode.HALF_EVEN), multiply(VALUE100, VALUE100));
    }

    @Test
    void shouldMultiplyValueZero() throws BelowZeroException {
        assertEquals(VALUE0.multiply(new BigDecimal(AFTER_COMMISSION))
                .multiply(VALUE100).setScale(2, RoundingMode.HALF_EVEN), multiply(VALUE0, VALUE100));
    }

    @Test
    void shouldMultiplyAskZero() throws BelowZeroException {
        assertEquals(VALUE100.multiply(new BigDecimal(AFTER_COMMISSION))
                .multiply(VALUE0).setScale(2, RoundingMode.HALF_EVEN), multiply(VALUE100, VALUE0));
    }

    @Test
    void shouldMultiplyBothZero() throws BelowZeroException {
        assertEquals(VALUE0.multiply(new BigDecimal(AFTER_COMMISSION))
                .multiply(VALUE0).setScale(2, RoundingMode.HALF_EVEN), multiply(VALUE0, VALUE0));
    }

    @Test
    void shouldNotMultiplyValueBelowZero() {
        try {
            multiply(VALUE_MINUS100, VALUE100);
            fail();
        } catch (BelowZeroException e) {
            assertTrue(true);
        }
    }

    @Test
    void shouldNotMultiplyAskBelowZero() {
        try {
            multiply(VALUE100, VALUE_MINUS100);
            fail();
        } catch (BelowZeroException e) {
            assertTrue(true);
        }
    }

    @Test
    void shouldDivide() throws BelowZeroException {
        assertEquals(VALUE100.multiply(new BigDecimal(AFTER_COMMISSION))
                .divide(VALUE100, 2, RoundingMode.HALF_EVEN), divide(VALUE100, VALUE100));
    }

    @Test
    void shouldDivideValueZero() throws BelowZeroException {
        assertEquals(VALUE0.multiply(new BigDecimal(AFTER_COMMISSION))
                .divide(VALUE100, 2, RoundingMode.HALF_EVEN), divide(VALUE0, VALUE100));
    }

    @Test
    void shouldNotDivideBidZero() throws BelowZeroException {
        try {
            divide(VALUE100, VALUE0);
            fail();
        } catch (NullPointerException e) {
            assertTrue(true);
        }
    }

    @Test
    void shouldNotDivideBothZero() {
        try {
            divide(VALUE0, VALUE0);
            fail();
        } catch (NullPointerException | BelowZeroException e) {
            assertTrue(true);
        }
    }

    @Test
    void shouldNotDivideValueBelowZero() {
        try {
            divide(VALUE_MINUS100, VALUE100);
            fail();
        } catch (BelowZeroException e) {
            assertTrue(true);
        }
    }

    @Test
    void shouldNotDivideBidBelowZero() {
        try {
            divide(VALUE100, VALUE_MINUS100);
            fail();
        } catch (BelowZeroException e) {
            assertTrue(true);
        }
    }

    @Test
    void shouldNotDivideBothBelowZero() {
        try {
            divide(VALUE_MINUS100, VALUE_MINUS100);
            fail();
        } catch (BelowZeroException e) {
            assertTrue(true);
        }
    }
}