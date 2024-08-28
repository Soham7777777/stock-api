package com.api.stock;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.PostConstruct;

import java.util.Optional;

@Service
class StockApiService {
    private final RestTemplate restTemplate;
    private String apiUrl;

    @Value("${API_KEY}")
    private String apiKey;


    StockApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostConstruct
    private void init() {
        apiUrl = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&apikey=" + apiKey + "&symbol=";
    }

    /**
     * return stock-quote fetched from the external API, if there is erro no symbol is empty String then return Optional.empty()
     * @param symbol : symbol for stock-quote
     * @return Optional<Quote> : desrialized quote from external source
     */
    Optional<Quote> fetchOne(String symbol) {
        if(symbol == "")
            return Optional.empty();
            
        try{
            ApiResponse response = restTemplate.getForObject(apiUrl+symbol, ApiResponse.class);
            if(response.getErrorMessage() == null && response.getQuote().getSymbol() != null)
                return Optional.of(response.getQuote());
            else
                return Optional.empty();    
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}