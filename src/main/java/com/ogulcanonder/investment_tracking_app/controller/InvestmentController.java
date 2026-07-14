package com.ogulcanonder.investment_tracking_app.controller;

import com.ogulcanonder.investment_tracking_app.dto.request.DtoInvestmentRequest;
import com.ogulcanonder.investment_tracking_app.dto.response.DtoInvestmentResponse;
import com.ogulcanonder.investment_tracking_app.dto.response.DtoInvestmentSummaryResponse;
import com.ogulcanonder.investment_tracking_app.service.AssetDetails;
import com.ogulcanonder.investment_tracking_app.service.InvestmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/investment")
@RequiredArgsConstructor
public class InvestmentController {
    private final InvestmentService investmentService;
    private final AssetDetails assetDetails;

    @PostMapping
    public ResponseEntity<DtoInvestmentResponse> addInvestment(
            @Valid @RequestBody DtoInvestmentRequest dtoInvestmentRequest) {
        DtoInvestmentResponse dtoInvestmentResponse = investmentService.create(dtoInvestmentRequest);
        return new ResponseEntity<>(dtoInvestmentResponse, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<DtoInvestmentSummaryResponse>> getInvestmentSummary() {
        List<DtoInvestmentSummaryResponse> dtoInvestmentSummaryResponseList = investmentService.getInvestmentSummary();
        return ResponseEntity.ok(dtoInvestmentSummaryResponseList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateInvestmentById(@PathVariable Long id,
                                                     @Valid @RequestBody DtoInvestmentRequest dtoInvestmentRequest) {
        investmentService.updateById(id, dtoInvestmentRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvestmentSummary(@PathVariable Long id) {
        investmentService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/totalassets")
    public ResponseEntity<BigDecimal> getTotalAssets() {
        BigDecimal totalAssets = assetDetails.totalAsset();
        return ResponseEntity.status(HttpStatus.OK).body(totalAssets);
    }
}
