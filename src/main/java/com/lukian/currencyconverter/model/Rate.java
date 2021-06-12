package com.lukian.currencyconverter.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Rate {
    private String currency;
    private Code code;
    private BigDecimal bid;
    private BigDecimal ask;
}
