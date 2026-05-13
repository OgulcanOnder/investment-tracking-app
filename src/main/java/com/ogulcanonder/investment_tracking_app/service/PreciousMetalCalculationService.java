package com.ogulcanonder.investment_tracking_app.service;

import java.math.BigDecimal;

public interface PreciousMetalCalculationService {
    BigDecimal gramGoldGetPrice();
    BigDecimal quarterGoldGetPrice();
    BigDecimal fullGoldGetPrice();
    BigDecimal gramSilverGetPrice();

}
