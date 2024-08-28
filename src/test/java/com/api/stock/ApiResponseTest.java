package com.api.stock;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ApiResponseTest {
	
	@Autowired
    private StockApiService apiService;
	
	/**
	 * Tests the deserialization of quote fetched from original data source
	 */
	@Test
	// @Disabled("This should only be enabled when testing communication with real api")
	void deserializationQuoteTest() {
        Optional<Quote> response = apiService.fetchOne("GOOGL");
        System.out.println(response);
		assertThat(response).isNotEqualTo(Optional.empty());
	}

    /**
     * Tests the deserialization of empty quote or error fetched from original data source
     */
    @Test
	// @Disabled("This should only be enabled when testing communication with real api")
	void deserializationEmptyQuoteTest() {
		Optional<Quote> response1 = apiService.fetchOne("LOL");
        System.out.println(response1);
		assertThat(response1).isEqualTo(Optional.empty());

		Optional<Quote> response2 = apiService.fetchOne("");
        System.out.println(response2);
		assertThat(response2).isEqualTo(Optional.empty());
	}
}