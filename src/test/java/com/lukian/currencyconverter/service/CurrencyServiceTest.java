package com.lukian.currencyconverter.service;

import com.lukian.currencyconverter.dto.ConvertDTO;
import com.lukian.currencyconverter.dto.CurrencyDTO;
import com.lukian.currencyconverter.exception.BelowZeroException;
import com.lukian.currencyconverter.exception.IdenticalCurrenciesException;
import com.lukian.currencyconverter.exception.RateNotFoundException;
import com.lukian.currencyconverter.model.Rate;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

import static com.lukian.currencyconverter.util.MathOperation.divide;
import static com.lukian.currencyconverter.util.MathOperation.multiply;
import static org.junit.jupiter.api.Assertions.*;

class CurrencyServiceTest {

    private final BigDecimal VALUE100 = new BigDecimal(100);
    private final BigDecimal VALUE_MINUS100 = new BigDecimal(-100);
    private final BigDecimal VALUE0 = new BigDecimal(0);
    private final String TOTAL_ZERO = "0.00";

    private final CurrencyService provider;
    private final Rate rate;
    private final Rate targetRate;

    CurrencyServiceTest() {
        provider = new CurrencyService(new RestTemplate());
        Rate[] rates = provider.getRates().toArray(new Rate[0]);
        rate = rates[0];
        targetRate = rates[1];
    }

    @Test
    void shouldGetAllRates() {
        System.out.println(provider.getRates());
        assertTrue(provider.getRates().toString().contains("code=USD")
                && provider.getRates().toString().contains("code=XDR"));
    }

    @Test
    void shouldCorrectlyCalculateBuyingCurrencyAmount() {
        var currencyDTO = new CurrencyDTO(VALUE100, rate.getCode());
        assertEquals(divide(VALUE100, rate.getAsk()), provider.buyCurrency(currencyDTO));
    }

    @Test
    void shouldPurchaseZeroCurrencyAmount() {
        var currencyDTO = new CurrencyDTO(VALUE0, rate.getCode());
        assertEquals(TOTAL_ZERO, provider.buyCurrency(currencyDTO).toString());
    }

    @Test
    void shouldNotPurchaseBelowZeroCurrencyAmount() {
        var currencyDTO = new CurrencyDTO(VALUE_MINUS100, rate.getCode());
        try {
            provider.buyCurrency(currencyDTO);
            fail();
        } catch (BelowZeroException e) {
            assertTrue(true);
        }
    }

    @Test
    void shouldCorrectlyCalculateSaleCurrencyAmount() {
        var currencyDTO = new CurrencyDTO(VALUE100, rate.getCode());
        assertEquals(multiply(VALUE100, rate.getBid()), provider.saleCurrency(currencyDTO));
    }

    @Test
    void shouldCalculateZeroSaleCurrencyAmount() {
        assertEquals(TOTAL_ZERO, provider.saleCurrency(new CurrencyDTO(VALUE0, rate.getCode())).toString());
    }

    @Test
    void shouldNotCalculateBelowZeroSaleCurrencyAmount() throws RateNotFoundException {
        var currencyDTO = new CurrencyDTO(VALUE_MINUS100, rate.getCode());
        try {
            provider.saleCurrency(currencyDTO);
            fail();
        } catch (BelowZeroException e) {
            assertTrue(true);
        }
    }

    @Test
    void shouldCorrectlyCalculateCurrencyConversion() {
        //when converting, the commission is charged 2 times:
        // when selling one currency and when buying another
        BigDecimal defaultCurrency = multiply(VALUE100, rate.getBid());
        var convertDTO = new ConvertDTO(new CurrencyDTO(VALUE100, rate.getCode()), targetRate.getCode());
        assertEquals(divide(defaultCurrency, targetRate.getAsk()), provider.convertCurrency(convertDTO));
    }

    @Test
    void shouldCalculateZeroCurrencyConversion() {
        var convertDTO = new ConvertDTO(new CurrencyDTO(VALUE0, rate.getCode()), targetRate.getCode());
        assertEquals(TOTAL_ZERO, provider.convertCurrency(convertDTO).toString());
    }

    @Test
    void shouldNotCalculateBelowZeroCurrencyConversion() {
        var convertDTO = new ConvertDTO(new CurrencyDTO(VALUE_MINUS100, rate.getCode()), targetRate.getCode());
        try {
            provider.convertCurrency(convertDTO);
            fail();
        } catch (BelowZeroException e) {
            assertTrue(true);
        }
    }

    @Test
    void shouldNotCalculateCurrencyConversionWithIdenticalRates() {
        var convertDTO = new ConvertDTO(new CurrencyDTO(VALUE100, rate.getCode()), rate.getCode());
        try {
            provider.convertCurrency(convertDTO);
            fail();
        } catch (IdenticalCurrenciesException e) {
            assertTrue(true);
        }
    }
}