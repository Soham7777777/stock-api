package com.api.stock;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
class ApiResponse {
    @JsonProperty("Global Quote")
    private Quote quote;

    @JsonProperty("Error Message")
    private String errorMessage;
}