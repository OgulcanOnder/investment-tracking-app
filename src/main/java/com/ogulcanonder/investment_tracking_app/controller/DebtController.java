package com.ogulcanonder.investment_tracking_app.controller;

import com.ogulcanonder.investment_tracking_app.dto.request.DtoDebtRequest;
import com.ogulcanonder.investment_tracking_app.dto.response.DtoDebtResponse;
import com.ogulcanonder.investment_tracking_app.service.DebtService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/debts")
public class DebtController {
    private final DebtService debtService;

    public DebtController(DebtService debtService) {
        this.debtService = debtService;
    }

    @PostMapping
    public ResponseEntity<DtoDebtResponse> createDebt(@Valid @RequestBody DtoDebtRequest dtoDebtRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(debtService.create(dtoDebtRequest));
    }

    @GetMapping
    public ResponseEntity<List<DtoDebtResponse>> getAllDebt() {
        return ResponseEntity.status(HttpStatus.OK).body(debtService.getAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDebtById(@PathVariable Long id) {
        debtService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateDebtById(@PathVariable Long id,
                                               @Valid @RequestBody DtoDebtRequest dtoDebtRequest) {
        debtService.updateById(id, dtoDebtRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/totaldebts")
    public ResponseEntity<BigDecimal> getTotalDebt() {
        return ResponseEntity.status(HttpStatus.OK).body(debtService.totalDebt());
    }
}
