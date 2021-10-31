package com.lukian.currencyconverter.service;

import com.lukian.currencyconverter.dto.ConvertDTO;
import com.lukian.currencyconverter.dto.CurrencyDTO;
import com.lukian.currencyconverter.exception.IdenticalCurrenciesException;
import com.lukian.currencyconverter.exception.RateNotFoundException;
import com.lukian.currencyconverter.exception.SourceNotFoundException;
import com.lukian.currencyconverter.model.Code;
import com.lukian.currencyconverter.model.Rate;
import com.lukian.currencyconverter.model.SourceTable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static com.lukian.currencyconverter.constant.Constant.SOURCE_URL;
import static com.lukian.currencyconverter.util.MathOperation.divide;
import static com.lukian.currencyconverter.util.MathOperation.multiply;

@Service
@Slf4j
public class CurrencyService {
    private final RestTemplate restTemplate;
    private final Set<Rate> rates;

    public CurrencyService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.rates = new HashSet<>();

        //the below construct is needed in case of api connection error
        try {
            this.rates.addAll(getAll());
            log.info("RATES INITIATED SUCCESSFULLY");
        } catch (SourceNotFoundException e) {
            log.error(e.getMessage().toUpperCase());
        }
    }

    public Set<Rate> getRates() {
        return rates;
    }

    public BigDecimal buyCurrency(CurrencyDTO currencyDTO) {
        log.info(String.format("VALUE IS VALID FOR %s BUYING", currencyDTO.getCode()));
        return divide(currencyDTO.getValue(), getRate(currencyDTO.getCode()).getAsk());
    }

    public BigDecimal saleCurrency(CurrencyDTO currencyDTO) {
        log.info(String.format("VALUE IS VALID FOR %s SALLYING", currencyDTO.getCode()));
        return multiply(currencyDTO.getValue(), getRate(currencyDTO.getCode()).getBid());
    }

    //to convert currency, we first sell it and then buy another
    public BigDecimal convertCurrency(ConvertDTO convertDTO) {
        if (isIdentical(convertDTO))
            throw new IdenticalCurrenciesException(
                    String.format("you selected identical currencies: %s", convertDTO.getTargetCode().name())
            );
        log.info("Current currency and target currency are not identical.");
        var defaultCurrency = saleCurrency(convertDTO.getCurrencyDTO());
        return buyCurrency(new CurrencyDTO(defaultCurrency, convertDTO.getTargetCode()));
    }

    private Set<Rate> getAll() {
        try {
            var json = restTemplate.getForObject(SOURCE_URL, SourceTable[].class);
            if (json == null) throw new SourceNotFoundException("source is empty");
            return json[0].getRates();
        } catch (HttpClientErrorException e) {
            throw new SourceNotFoundException("source url is invalid!");
        }
    }

    private Rate getRate(Code code) {
        return getRates().stream()
                .filter(r -> r.getCode() == code)
                .findFirst()
                .orElseThrow(() -> new RateNotFoundException(String.format("Rate with name: %s was not found", code)));
    }

    private boolean isIdentical(ConvertDTO convertDTO) {
        return convertDTO.getCurrencyDTO().getCode() == convertDTO.getTargetCode();
    }
}
