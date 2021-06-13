package com.lukian.currencyconverter.constant;

public class Constant {
    private Constant() { }

    //if you change the constant below, there will be SourceNotFoundException in the CurrencyService.class
    // and therefore the entire network service will not work correctly
    public static final String SOURCE_URL = "http://api.nbp.pl/api/exchangerates/tables/c/?format=json";
    public static final String AFTER_COMMISSION = "0.98";
    public static final String DEFAULT_CURRENCY_SYMBOL = "zł";
}
