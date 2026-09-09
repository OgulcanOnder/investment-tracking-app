package com.ogulcanonder.investment_tracking_app.service.impl;

import com.ogulcanonder.investment_tracking_app.service.DebtService;
import com.ogulcanonder.investment_tracking_app.service.FinancialSummaryService;
import com.ogulcanonder.investment_tracking_app.service.InvestmentService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class FinancialSummaryServiceImpl implements FinancialSummaryService {
    private final InvestmentService investmentService;
    private final DebtService debtService;

    public FinancialSummaryServiceImpl(InvestmentService investmentService, DebtService debtService) {
        this.investmentService = investmentService;
        this.debtService = debtService;
    }

    @Override
    public BigDecimal totalNetAssets() {
        return investmentService.totalInvestmentAssets().subtract(debtService.totalDebt());
    }
}
