package com.lukian.currencyconverter.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class SourceTable {
    private String table;
    private String no;
    private String tradingDate;
    private String effectiveDate;
    private List<Rate> rates;
}
