package com.api.stock;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.boot.jackson.JsonComponent;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
public class Quote {
    @JsonProperty("01. symbol")
    @Schema(name = "symbol")
    private String symbol;

    @JsonProperty("02. open")
    @Schema(name = "open")
    private double open;

    @JsonProperty("03. high")
    @Schema(name = "high")
    private double high;

    @JsonProperty("04. low")
    @Schema(name = "low")
    private double low;

    @JsonProperty("05. price")
    @Schema(name = "price")
    private double price;

    @JsonProperty("06. volume")
    @Schema(name = "volume")
    private int volume;

    @JsonProperty("07. latest trading day")
    @Schema(name = "latest trading day")
    private LocalDate latestTradingDay;

    @JsonProperty("08. previous close")
    @Schema(name = "previous close")
    private double previousClose;

    @JsonProperty("09. change")
    @Schema(name = "change")
    private double change;

    @JsonProperty("10. change percent")
    @Schema(name = "change percent")
    private String changePercent;
}


@JsonComponent
class CustomJsonComponent {

	public static class Serializer extends JsonSerializer<Quote> {

		@Override
        public void serialize(Quote value, JsonGenerator jgen, SerializerProvider serializers) throws IOException {
            jgen.writeStartObject();
            Map<String, Object> fields = serializers.getConfig().introspect(serializers.constructType(Quote.class))
                                                     .findProperties()
                                                     .stream()
                                                     .collect(Collectors.toMap(
                                                         prop -> removePrefix(prop.getName(), 4),
                                                         prop -> {
                                                             try {
                                                                 return prop.getGetter().callOn(value);
                                                             } catch (Exception e) {
                                                                 return null;
                                                             }
                                                         }
                                                     ));
            for (Map.Entry<String, Object> entry : fields.entrySet()) {
                jgen.writeObjectField(entry.getKey(), entry.getValue());
            }
            jgen.writeEndObject();
        }

        private String removePrefix(String fieldName, int length) {
            return fieldName.length() > length ? fieldName.substring(length) : fieldName;
        }

	}
}