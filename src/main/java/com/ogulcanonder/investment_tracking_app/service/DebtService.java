package com.ogulcanonder.investment_tracking_app.service;

import com.ogulcanonder.investment_tracking_app.dto.request.DtoDebtRequest;
import com.ogulcanonder.investment_tracking_app.dto.response.DtoDebtResponse;

import java.util.List;

public interface DebtService {
    public DtoDebtResponse create(DtoDebtRequest dtoDebtRequest);

    public List<DtoDebtResponse> getAll();

    public void deleteById(Long id);

    public void updateById(Long id, DtoDebtRequest dtoDebtRequest);
}
