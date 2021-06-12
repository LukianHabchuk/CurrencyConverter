package com.lukian.currencyconverter.model;

public enum Code {
    USD(" \u0024"), AUD(" A\u0024"), CAD(" C\u0024"),
    EUR(" \u20AC"), HUF(" Ft"), CHF(" \u20A3"),
    GBP(" \u00A3"), JPY(" \u00A5"), CZK(" Kč"),
    DKK(" kr"), NOK(" kr"), SEK(" kr"), XDR(" SDR");

    private final String symbol;

    Code(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}
