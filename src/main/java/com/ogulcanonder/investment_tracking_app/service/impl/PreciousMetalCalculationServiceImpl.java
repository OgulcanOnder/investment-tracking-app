package com.ogulcanonder.investment_tracking_app.service.impl;

import com.ogulcanonder.investment_tracking_app.service.PreciousMetalCalculationService;
import com.ogulcanonder.investment_tracking_app.service.MarketDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class PreciousMetalCalculationServiceImpl implements PreciousMetalCalculationService {
    private final MarketDataService marketDataService;

    private static final BigDecimal GRAM_GOLD_SILVER_NUMBER = BigDecimal.valueOf(31.10);
    private static final int FINANCIAL_CALCULATION_SCALE = 4;
    private static final BigDecimal QUARTER_GOLD_NUMBER = BigDecimal.valueOf(1.605);
    private static final int QUARTER_GOLD_ADDITION = 250;
    private static final int FULL_GOLD_NUMBER = 4;

    @Override
    public BigDecimal gramGoldGetPrice() {

        BigDecimal ons = marketDataService.getPrice("GC=F");
        BigDecimal usdTry = marketDataService.getPrice("USDTRY=X");
        if (ons == null || usdTry == null) {
            return null;
        }
        return ons
                .divide(GRAM_GOLD_SILVER_NUMBER, FINANCIAL_CALCULATION_SCALE, RoundingMode.HALF_UP)
                .multiply(usdTry);
    }

    @Override
    public BigDecimal quarterGoldGetPrice() {
        BigDecimal gramGoldPrice = gramGoldGetPrice();
        if (gramGoldPrice == null) {
            return null;
        }
        return gramGoldPrice
                .multiply(QUARTER_GOLD_NUMBER)
                .add(BigDecimal.valueOf(QUARTER_GOLD_ADDITION))
                .setScale(FINANCIAL_CALCULATION_SCALE, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal fullGoldGetPrice() {
        BigDecimal quarterGoldPrice = quarterGoldGetPrice();
        if (quarterGoldPrice == null) {
            return null;
        }
        return quarterGoldPrice
                .multiply(BigDecimal.valueOf(FULL_GOLD_NUMBER))
                .setScale(FINANCIAL_CALCULATION_SCALE, RoundingMode.HALF_UP);

    }

    @Override
    public BigDecimal gramSilverGetPrice() {
        BigDecimal ons = marketDataService.getPrice("SI=F");
        BigDecimal usdTry = marketDataService.getPrice("USDTRY=X");
        if (ons == null || usdTry == null) {
            return null;
        }
        return ons
                .divide(GRAM_GOLD_SILVER_NUMBER, FINANCIAL_CALCULATION_SCALE, RoundingMode.HALF_UP)
                .multiply(usdTry)
                .setScale(FINANCIAL_CALCULATION_SCALE, RoundingMode.HALF_UP);
    }
}
