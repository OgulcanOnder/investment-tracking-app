package com.ogulcanonder.investment_tracking_app.service;

import java.math.BigDecimal;

public interface InstrumentPriceService {

    public BigDecimal getPrice(String apiSymbol);
}
