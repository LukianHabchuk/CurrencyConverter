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
import java.math.RoundingMode;

import static com.lukian.currencyconverter.util.Utility.commission;
import static org.junit.jupiter.api.Assertions.*;

class CurrencyServiceTest {

    private final BigDecimal VALUE100 = new BigDecimal(100);
    private final BigDecimal VALUE_MINUS100 = new BigDecimal(-100);
    private final BigDecimal VALUE0 = new BigDecimal(0);
    private final String TOTAL_ZERO = "0.00";

    private final CurrencyService provider;

    CurrencyServiceTest() {
        provider = new CurrencyService(new RestTemplate());
    }

    @Test
    void shouldGetAllRates() {
        System.out.println(provider.getRates());
        assertTrue(provider.getRates().toString().contains("code=USD") && provider.getRates().toString().contains("code=XDR"));
    }

    @Test
    void shouldCorrectlyCalculateBuyingCurrencyAmount() throws RateNotFoundException, BelowZeroException {
        Rate rate = provider.getRates().get(2);
        assertEquals(commission(VALUE100).divide(rate.getAsk(), 2, RoundingMode.HALF_EVEN),
                provider.buyCurrency(new CurrencyDTO(VALUE100, rate.getCode())));
    }

    @Test
    void shouldPurchaseZeroCurrencyAmount() throws RateNotFoundException, BelowZeroException {
        Rate rate = provider.getRates().get(2);
        assertEquals(TOTAL_ZERO, provider.buyCurrency(new CurrencyDTO(VALUE0, rate.getCode())).toString());
    }

    @Test
    void shouldNotPurchaseBelowZeroCurrencyAmount() throws RateNotFoundException {
        Rate rate = provider.getRates().get(2);
        try {
            provider.buyCurrency(new CurrencyDTO(VALUE_MINUS100, rate.getCode()));
        }  catch (BelowZeroException e) {
            assertTrue(true);
        }
    }

    @Test
    void shouldCorrectlyCalculateSaleCurrencyAmount() throws RateNotFoundException, BelowZeroException {
        Rate rate = provider.getRates().get(3);
        assertEquals(
                commission(VALUE100).multiply(rate.getBid()).divide(BigDecimal.valueOf(1), 2, RoundingMode.HALF_EVEN),
                provider.saleCurrency(new CurrencyDTO(VALUE100, rate.getCode()))
        );
    }

    @Test
    void shouldCalculateZeroSaleCurrencyAmount() throws RateNotFoundException, BelowZeroException {
        Rate rate = provider.getRates().get(3);
        assertEquals(TOTAL_ZERO, provider.saleCurrency(new CurrencyDTO(VALUE0, rate.getCode())).toString());
    }

    @Test
    void shouldNotCalculateBelowZeroSaleCurrencyAmount() throws RateNotFoundException {
        Rate rate = provider.getRates().get(3);
        try {
            provider.saleCurrency(new CurrencyDTO(VALUE_MINUS100, rate.getCode()));
        } catch (BelowZeroException e) {
            assertTrue(true);
        }
    }

    @Test
    void shouldCorrectlyCalculateCurrencyConversion() throws RateNotFoundException, BelowZeroException, IdenticalCurrenciesException {
        Rate rate = provider.getRates().get(3);
        Rate targetRate = provider.getRates().get(5);
        //when converting, the commission is charged 2 times:
        // when selling one currency and when buying another
        assertEquals(commission(commission(VALUE100)).multiply(rate.getBid())
                        .divide(targetRate.getAsk(), 2, RoundingMode.HALF_EVEN),
                provider.convertCurrency(new ConvertDTO(new CurrencyDTO(VALUE100, rate.getCode()),targetRate.getCode())));
    }

    @Test
    void shouldCalculateZeroCurrencyConversion() throws RateNotFoundException, BelowZeroException, IdenticalCurrenciesException {
        Rate rate = provider.getRates().get(3);
        Rate targetRate = provider.getRates().get(5);
        assertEquals(TOTAL_ZERO, provider.convertCurrency(new ConvertDTO(new CurrencyDTO(VALUE0, rate.getCode()), targetRate.getCode())).toString());
    }

    @Test
    void shouldNotCalculateBelowZeroCurrencyConversion() throws RateNotFoundException, IdenticalCurrenciesException {
        Rate rate = provider.getRates().get(3);
        Rate targetRate = provider.getRates().get(5);
        try {
            provider.convertCurrency(new ConvertDTO(new CurrencyDTO(VALUE_MINUS100, rate.getCode()),targetRate.getCode()));
        } catch (BelowZeroException e) {
            assertTrue(true);
        }
    }

    @Test
    void shouldNotCalculateCurrencyConversionWithIdenticalRates() throws RateNotFoundException, BelowZeroException {
        Rate rate = provider.getRates().get(3);
        try {
            provider.convertCurrency(new ConvertDTO(new CurrencyDTO(VALUE100, rate.getCode()), rate.getCode()));
        } catch (IdenticalCurrenciesException e) {
            assertTrue(true);
        }
    }
}