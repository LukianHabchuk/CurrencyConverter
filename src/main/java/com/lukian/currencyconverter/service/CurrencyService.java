package com.lukian.currencyconverter.service;

import com.lukian.currencyconverter.dto.ConvertDTO;
import com.lukian.currencyconverter.dto.CurrencyDTO;
import com.lukian.currencyconverter.exception.BelowZeroException;
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
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static com.lukian.currencyconverter.constant.Constant.SOURCE_URL;
import static com.lukian.currencyconverter.util.Utility.*;

@Service
@Slf4j
public class CurrencyService {
    private final RestTemplate restTemplate;
    private final List<Rate> rates;

    public CurrencyService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.rates = new ArrayList<>();

        //the below construct is needed in case of api connection error
        try {
            this.rates.addAll(getAll());
            log.info("RATES INITIATED SUCCESSFULLY");
        } catch (SourceNotFoundException e) {
            log.error(e.getMessage().toUpperCase());
        }
    }

    public List<Rate> getRates() {
        return rates;
    }

    public BigDecimal buyCurrency(CurrencyDTO currencyDTO) throws RateNotFoundException, BelowZeroException {
        validate(currencyDTO.getValue());
        log.info(String.format("VALUE IS VALID FOR %s BUYING", currencyDTO.getCode()));
        return commission(currencyDTO.getValue())
                .divide(getRate(currencyDTO.getCode()).getAsk(), 2, RoundingMode.HALF_EVEN);
    }

    public BigDecimal saleCurrency(CurrencyDTO currencyDTO) throws RateNotFoundException, BelowZeroException {
        validate(currencyDTO.getValue());
        log.info(String.format("VALUE IS VALID FOR %s SALLYING", currencyDTO.getCode()));
        return commission(currencyDTO.getValue())
                .multiply(getRate(currencyDTO.getCode()).getBid())
                .divide(BigDecimal.valueOf(1), 2, RoundingMode.HALF_EVEN);
    }

    //to convert currency, we first sell it and then buy another
    public BigDecimal convertCurrency(ConvertDTO convertDTO) throws RateNotFoundException, BelowZeroException, IdenticalCurrenciesException {
        if (!isIdentical(convertDTO.getCurrencyDTO().getCode(), convertDTO.getTargetCode())) {
            log.info("Current currency and target currency are not identical.");
            BigDecimal defaultCurrency = saleCurrency(convertDTO.getCurrencyDTO());
            return buyCurrency(new CurrencyDTO(defaultCurrency, convertDTO.getTargetCode()));
        } else throw new IdenticalCurrenciesException(
                String.format("you selected identical currencies: %s", convertDTO.getTargetCode().name()));
    }

    private List<Rate> getAll() throws SourceNotFoundException {
        try {
            SourceTable[] json = restTemplate.getForObject(SOURCE_URL, SourceTable[].class);
            if (json == null) throw new SourceNotFoundException("source is empty");
            else return json[0].getRates();
        } catch (HttpClientErrorException e) {
            throw new SourceNotFoundException("source url is invalid!");
        }
    }

    private Rate getRate(Code code) throws RateNotFoundException {
        return getRates().stream()
                .filter(r -> r.getCode() == code)
                .findFirst()
                .orElseThrow(() -> new RateNotFoundException(String.format("Rate with name: %s was not found", code)));
    }
}
