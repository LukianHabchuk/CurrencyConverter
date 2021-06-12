package com.lukian.currencyconverter.dto;

import com.lukian.currencyconverter.model.Code;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CurrencyDTO {
    private BigDecimal value;
    private Code code;
}
