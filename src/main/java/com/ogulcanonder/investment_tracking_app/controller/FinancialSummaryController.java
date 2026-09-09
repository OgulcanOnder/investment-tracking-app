package com.ogulcanonder.investment_tracking_app.controller;

import com.ogulcanonder.investment_tracking_app.service.FinancialSummaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/summary")
public class FinancialSummaryController {
    private final FinancialSummaryService financialSummaryService;

    public FinancialSummaryController(FinancialSummaryService financialSummaryService) {
        this.financialSummaryService = financialSummaryService;
    }

    @GetMapping
    public ResponseEntity<BigDecimal> totalNetAssets() {
        return ResponseEntity.ok(financialSummaryService.totalNetAssets());
    }
}
