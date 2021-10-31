package com.lukian.currencyconverter.resource;

import com.lukian.currencyconverter.dto.ConvertDTO;
import com.lukian.currencyconverter.dto.CurrencyDTO;
import com.lukian.currencyconverter.exception.ExceptionHandling;
import com.lukian.currencyconverter.model.Rate;
import com.lukian.currencyconverter.service.CurrencyService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

import static com.lukian.currencyconverter.constant.Constant.DEFAULT_CURRENCY_SYMBOL;
import static com.lukian.currencyconverter.constant.HttpConstant.*;

@RestController
@AllArgsConstructor
@ApiResponses(
        value = {
                @ApiResponse(code = BAD_REQUEST_CODE, message = BAD_REQUEST_MESSAGE),
                @ApiResponse(code = SERVER_ERROR_CODE, message = SERVER_ERROR_MESSAGE)
        })
public class ConverterResource extends ExceptionHandling {
    private final CurrencyService provider;

    @GetMapping("/")
    @Operation(description = "Get all currencies")
    public ResponseEntity<Set<Rate>> getAll() {
        return new ResponseEntity<>(provider.getRates(), HttpStatus.OK);
    }

    @PostMapping("/buy")
    @Operation(description = "buy foreign currency from default currency", summary = "buy currency")
    public ResponseEntity<String> buyCurrency(@RequestBody CurrencyDTO currencyDTO) {
        return new ResponseEntity<>(provider.buyCurrency(currencyDTO)
                + currencyDTO.getCode().getSymbol(), HttpStatus.OK);
    }

    @PostMapping("/sale")
    @Operation(description = "buy default currency from foreign currency", summary = "sale currency")
    public ResponseEntity<String> saleCurrency(@RequestBody CurrencyDTO currencyDTO) {
        return new ResponseEntity<>(provider.saleCurrency(currencyDTO) + DEFAULT_CURRENCY_SYMBOL, HttpStatus.OK);
    }

    @PostMapping("/convert")
    @Operation(description = "conversion of one foreign currency to another", summary = "convert currency")
    @ApiResponses(value = @ApiResponse(code = NOT_ACCEPTABLE_CODE, message = NOT_ACCEPTABLE_MESSAGE))
    public ResponseEntity<String> convertCurrency(@RequestBody ConvertDTO convertDTO) {
        return new ResponseEntity<>(provider.convertCurrency(convertDTO)
                + convertDTO.getTargetCode().getSymbol(), HttpStatus.OK);
    }
}
