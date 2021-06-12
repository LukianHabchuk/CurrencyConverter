package com.lukian.currencyconverter.dto;

import com.lukian.currencyconverter.model.Code;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;


@Data
@AllArgsConstructor
@Getter
public class ConvertDTO {
    private CurrencyDTO currencyDTO;
    private Code targetCode;
}
