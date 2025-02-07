package com.api.stock;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

@WebMvcTest(controllers = StockController.class)
class StockControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private StockApiService apiService;

	@Autowired
	private ObjectMapper objectMapper;

	/**
	 * Used to generated deserialized mock data for quote that orginially deserialized by StockApiService 
	 * @param symbol : symbol to create the Quote object 
	 * @return Optional<Quote> : The mocked version of data returned by StockApiService::fetchOne
	 */
	private Optional<Quote> getMockQuote(String symbol) {
		Quote quote = new Quote();
		quote.setSymbol(symbol);
        quote.setOpen(196.0000);
        quote.setHigh(198.3450);
        quote.setLow(195.9000);
        quote.setPrice(197.9800);
        quote.setVolume(2567217);
        quote.setLatestTradingDay(LocalDate.of(2024, 8, 26));
        quote.setPreviousClose(196.1000);
        quote.setChange(1.8800);
        quote.setChangePercent("0.9587%");
		return Optional.of(quote);
	}

	/**
	 * Tests that StockController::getOneBySymbol controller method should sucessfully fetch a quote
	 * @throws Exception : Exception generated by mockMvc
	 */
	@Test
	@WithMockUser
	void shouldFetchOne() throws Exception{
		Optional<Quote> mockQuote = getMockQuote("IBM");
		when(apiService.fetchOne("IBM")).thenReturn(mockQuote);

		mockMvc.perform(MockMvcRequestBuilders.get("/quote/IBM")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(mockQuote.get())));
	}

	/**
	 * Tests that StockController::getOneBySymbol controller method should return NotFound response for cases where no symbol is specified or symbol that does not exists
	 * @throws Exception : Exception generated by mockMvc
	 */
	@Test
	@WithMockUser
	void shouldNotFetchOne() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.get("/quote/")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isNotFound());

		when(apiService.fetchOne("LOL")).thenReturn(Optional.empty());

		mockMvc.perform(MockMvcRequestBuilders.get("/quote/LOL")
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	/**
	 * Tests that StockController::getBatchQuotes returns set of stock-quotes for all symbols requested, and should not include the quotes which don't exists
	 * @throws Exception : Exception generated by mockMvc
	 */
	@Test
	@WithMockUser
	void shouldFetchBatch() throws Exception {
		List<Optional<Quote>> quotes = new ArrayList<>(4);
		quotes.add(getMockQuote("IBM"));
		quotes.add(getMockQuote("GOOG"));
		quotes.add(getMockQuote("AMZN"));

		when(apiService.fetchOne("IBM")).thenReturn(quotes.get(0));
		when(apiService.fetchOne("GOOG")).thenReturn(quotes.get(1));
		when(apiService.fetchOne("AMZN")).thenReturn(quotes.get(2));

		Collections.swap(quotes, 0, 2);
		Collections.swap(quotes, 0, 1);
		mockMvc.perform(MockMvcRequestBuilders.get("/quote/batch?symbols=IBM,GOOG,AMZN")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(quotes)));

		when(apiService.fetchOne("LOL")).thenReturn(Optional.empty());
		mockMvc.perform(MockMvcRequestBuilders.get("/quote/batch?symbols=LOL")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string("[]"));
	}
}