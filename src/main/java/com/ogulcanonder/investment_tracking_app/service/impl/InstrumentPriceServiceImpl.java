package com.ogulcanonder.investment_tracking_app.service.impl;

import com.ogulcanonder.investment_tracking_app.config.CacheConfig;
import com.ogulcanonder.investment_tracking_app.service.InstrumentPriceService;
import com.ogulcanonder.investment_tracking_app.service.MarketDataService;
import com.ogulcanonder.investment_tracking_app.service.PreciousMetalCalculationService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional(readOnly = true)
public class InstrumentPriceServiceImpl implements InstrumentPriceService {

    private final PreciousMetalCalculationService preciousMetalCalculationService;
    private final MarketDataService marketDataService;

    public InstrumentPriceServiceImpl(PreciousMetalCalculationService preciousMetalCalculationService,
                                      MarketDataService marketDataService) {
        this.preciousMetalCalculationService = preciousMetalCalculationService;
        this.marketDataService = marketDataService;
    }

    @Cacheable(cacheNames = CacheConfig.CACHE_NAME, key = "#apiSymbol")
    @Override
    public BigDecimal getPrice(String apiSymbol) {
        BigDecimal price;
        switch (apiSymbol) {
            case "GRAM_ALTIN":
                price = preciousMetalCalculationService.gramGoldGetPrice();
                break;
            case "CEYREK_ALTIN":
                price = preciousMetalCalculationService.quarterGoldGetPrice();
                break;
            case "TAM_ALTIN":
                price = preciousMetalCalculationService.fullGoldGetPrice();
                break;
            case "GRAM_GUMUS":
                price = preciousMetalCalculationService.gramSilverGetPrice();
                break;
            default:
                price = marketDataService.getPrice(apiSymbol);
        }
        return price;
    }
}
