package com.lukian.currencyconverter.util;

import com.lukian.currencyconverter.exception.BelowZeroException;
import com.lukian.currencyconverter.model.Code;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class UtilityTest {

    private final BigDecimal VALUE100 = new BigDecimal(100);
    private final BigDecimal VALUE_MINUS100 = new BigDecimal(-100);
    private final BigDecimal VALUE0 = new BigDecimal(0);

    private final Code code1 = Code.CAD;
    private final Code code2 = Code.USD;

    @Test
    void shouldBeIdentical() {
        assertTrue(Utility.isIdentical(code1, code1));
    }

    @Test
    void shouldNotBeIdentical() {
        assertFalse(Utility.isIdentical(code1, code2));
    }

    @Test
    void shouldBeValid() {
        try {
            Utility.validate(VALUE100);
            assertTrue(true);
        } catch (BelowZeroException e) {
            fail();
        }
    }

    @Test
    void shouldBeValidWithZero() {
        try {
            Utility.validate(VALUE0);
            assertTrue(true);
        } catch (BelowZeroException e) {
            fail();
        }
    }

    @Test
    void shouldNotBeValid() {
        try {
            Utility.validate(VALUE_MINUS100);
            fail();
        } catch (BelowZeroException e) {
            assertTrue(true);
        }
    }

    @Test
    void commission() {
        assertEquals(VALUE100.multiply(new BigDecimal("0.98")), Utility.commission(VALUE100));
    }
}