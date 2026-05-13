package com.ogulcanonder.investment_tracking_app.service;

import com.ogulcanonder.investment_tracking_app.dto.request.DtoInstrumentsRequest;
import com.ogulcanonder.investment_tracking_app.dto.response.DtoInstrumentsResponse;
import com.ogulcanonder.investment_tracking_app.entity.Instruments;

import java.math.BigDecimal;
import java.util.List;

public interface InstrumentsService {
    DtoInstrumentsResponse create(DtoInstrumentsRequest dtoInstrumentsRequest);
    List<DtoInstrumentsResponse> getAll();
    DtoInstrumentsResponse getInstrumentsById(Long id);
    BigDecimal apiSymbolMatching(String apiSymbol);
    Instruments getInstrumentsEntityById(Long id);
    void updateById(Long id, DtoInstrumentsRequest dtoInstrumentsRequest);
    void deleteById(Long id);

}
