package com.ogulcanonder.investment_tracking_app.service;

import com.ogulcanonder.investment_tracking_app.dto.request.DtoInvestmentRequest;
import com.ogulcanonder.investment_tracking_app.dto.response.DtoInvestmentResponse;
import com.ogulcanonder.investment_tracking_app.dto.response.DtoInvestmentSummaryResponse;
import com.ogulcanonder.investment_tracking_app.entity.Investment;

import java.util.List;

public interface InvestmentService {
    DtoInvestmentResponse create(DtoInvestmentRequest dtoInvestmentRequest);
    List<DtoInvestmentSummaryResponse> getInvestmentSummary();
    DtoInvestmentSummaryResponse buildSummary(List<Investment> investments);
    void updateById(Long id, DtoInvestmentRequest dtoInvestmentRequest);
    void deleteById(Long id);
}
