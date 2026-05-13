package com.ogulcanonder.investment_tracking_app.service.impl;

import com.ogulcanonder.investment_tracking_app.dto.response.DtoInvestmentSummaryResponse;

import com.ogulcanonder.investment_tracking_app.service.AssetDetails;
import com.ogulcanonder.investment_tracking_app.service.InvestmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssetDetailsImpl implements AssetDetails {

    private final InvestmentService investmentService;

    @Override
    public BigDecimal totalAsset() {
        BigDecimal totalAsset = BigDecimal.ZERO;
        List<DtoInvestmentSummaryResponse> investmentList = investmentService.getInvestmentSummary();
        for (DtoInvestmentSummaryResponse value : investmentList) {
            totalAsset = totalAsset.add(value.totalValue());
        }
        return totalAsset;
    }
}
