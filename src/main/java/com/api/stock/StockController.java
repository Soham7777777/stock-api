package com.api.stock;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Optional;


@RestController
@RequestMapping("quote")
class StockController {

    private final StockApiService apiService;

    StockController(StockApiService apiService, ObjectMapper objectMapper) {
        this.apiService = apiService;
    }

    /**
     * Controller method for fetching single stock-quote
     * @param symbol : symbol for stock-quote
     * @return ResponseEntity<Quote> : The stock-quote of given symbol fetched from external data source
     */
    @GetMapping("/{symbol}")
    private ResponseEntity<Quote> getOneBySymbol(@PathVariable String symbol) {
        Optional<Quote> serviceResponse = apiService.fetchOne(symbol);

        if (serviceResponse.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(serviceResponse.get());
    }

    /**
     * Controller method for fetching stock-quotes in batch
     * @param symbols : list of symbols for stock-quotes
     * @return ResponseEntity<Set<Quote>>: The set of stock-quotes fetched from external data source for all symbols requested
     */
    @GetMapping("/batch")
    public ResponseEntity<Set<Quote>> getBatchQuotes(@RequestParam List<String> symbols) {
        Set<Quote> uniqueQuotes = new HashSet<>();

        for (String symbol : symbols) {
            Optional<Quote> serviceResponse = apiService.fetchOne(symbol);
            serviceResponse.ifPresent(uniqueQuotes::add);
        }

        return ResponseEntity.ok(uniqueQuotes);
    }
}
