package com.ogulcanonder.investment_tracking_app.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ogulcanonder.investment_tracking_app.service.MarketDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
@RequiredArgsConstructor
public class YahooFinanceService implements MarketDataService {
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public BigDecimal getPrice(String symbol) {
        String url = "https://query1.finance.yahoo.com/v8/finance/chart/" + symbol;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .header("User-Agent", "Mozilla/5.0")
                .build();

        try {
            HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            return extractPrice(response.body());

        } catch (Exception e) {
            throw new RuntimeException("Yahoo Finance request failed", e);
        }
    }

    private BigDecimal extractPrice(String json) throws Exception {

        JsonNode root = objectMapper.readTree(json);

        JsonNode meta = root
                .path("chart")
                .path("result")
                .get(0)
                .path("meta");

        if (meta.isMissingNode() || meta.isEmpty()) {
            return null;
        }

        return meta
                .path("regularMarketPrice")
                .decimalValue();
    }
}
