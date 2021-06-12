package com.lukian.currencyconverter.resource;

import com.lukian.currencyconverter.dto.ConvertDTO;
import com.lukian.currencyconverter.dto.CurrencyDTO;
import com.lukian.currencyconverter.exception.BelowZeroException;
import com.lukian.currencyconverter.exception.IdenticalCurrenciesException;
import com.lukian.currencyconverter.exception.RateNotFoundException;
import com.lukian.currencyconverter.model.Rate;
import com.lukian.currencyconverter.service.CurrencyService;
import com.lukian.currencyconverter.exception.ExceptionHandling;
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

import java.util.List;

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
    public ResponseEntity<List<Rate>> getAll() {
        return new ResponseEntity<>(provider.getRates(), HttpStatus.OK);
    }

    @PostMapping("/buy")
    @Operation(description = "buy of foreign currency from default currency", summary = "buy currency")
    public ResponseEntity<String> buyCurrency(@RequestBody CurrencyDTO currencyDTO)
            throws RateNotFoundException, BelowZeroException {
        return new ResponseEntity<>(provider.buyCurrency(currencyDTO)
                + currencyDTO.getCode().getSymbol(), HttpStatus.OK);
    }

    @PostMapping("/sale")
    @Operation(description = "buy of default currency from foreign currency", summary = "sale currency")
    public ResponseEntity<String> saleCurrency(@RequestBody CurrencyDTO currencyDTO)
            throws RateNotFoundException, BelowZeroException {
        return new ResponseEntity<>(provider.saleCurrency(currencyDTO) + "zł", HttpStatus.OK);
    }

    @PostMapping("/convert")
    @Operation(description = "conversion of one foreign currency to another", summary = "convert currency")
    @ApiResponses(value = @ApiResponse(code = NOT_ACCEPTABLE_CODE, message = NOT_ACCEPTABLE_MESSAGE))
    public ResponseEntity<String> convertCurrency(@RequestBody ConvertDTO convertDTO)
            throws RateNotFoundException, BelowZeroException, IdenticalCurrenciesException {
        return new ResponseEntity<>(provider.convertCurrency(convertDTO)
                + convertDTO.getTargetCode().getSymbol(), HttpStatus.OK);
    }
}
