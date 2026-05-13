package com.ogulcanonder.investment_tracking_app.service;

import java.math.BigDecimal;

public interface MarketDataService {
    BigDecimal getPrice(String symbol);
}
