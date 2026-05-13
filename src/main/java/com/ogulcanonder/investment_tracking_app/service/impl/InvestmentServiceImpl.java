package com.ogulcanonder.investment_tracking_app.service.impl;

import com.ogulcanonder.investment_tracking_app.dto.request.DtoInvestmentRequest;
import com.ogulcanonder.investment_tracking_app.dto.response.DtoInvestmentResponse;
import com.ogulcanonder.investment_tracking_app.dto.response.DtoInvestmentSummaryResponse;
import com.ogulcanonder.investment_tracking_app.entity.Instruments;
import com.ogulcanonder.investment_tracking_app.entity.Investment;
import com.ogulcanonder.investment_tracking_app.mapper.InvestmentMapper;
import com.ogulcanonder.investment_tracking_app.repository.InvestmentRepository;
import com.ogulcanonder.investment_tracking_app.service.InstrumentsService;
import com.ogulcanonder.investment_tracking_app.service.InvestmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InvestmentServiceImpl implements InvestmentService {
    private final InvestmentRepository investmentRepository;
    private final InvestmentMapper investmentMapper;
    private final InstrumentsService instrumentsService;

    private static final int FINANCIAL_CALCULATION_SCALE = 4;

    @Transactional
    @Override
    public DtoInvestmentResponse create(DtoInvestmentRequest dtoInvestmentRequest) {
        Instruments instruments = instrumentsService.getInstrumentsEntityById(dtoInvestmentRequest.instrumentsId());
        Investment investment = investmentMapper.toEntity(dtoInvestmentRequest, instruments);
        investment.setBuyDate(LocalDateTime.now());
        return investmentMapper.toDto(investmentRepository.save(investment));
    }

    @Transactional
    @Override
    public List<DtoInvestmentSummaryResponse> getInvestmentSummary() {
        return investmentRepository.findAll().stream().collect(Collectors.groupingBy(inv ->
                inv.getInstruments().getId())).entrySet().stream().map(entry ->
                buildSummary(entry.getValue())).toList();
    }

    @Override
    public DtoInvestmentSummaryResponse buildSummary(List<Investment> investments) {
        Instruments instruments = investments.get(0).getInstruments();

        BigDecimal totalQuantity = investments.stream().map(Investment::getQuantity)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal weightedSum = investments.stream().map(inv -> inv.getQuantity()
                .multiply(inv.getBuyPrice())).reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal averageCost = weightedSum.divide(totalQuantity, FINANCIAL_CALCULATION_SCALE, RoundingMode.HALF_UP);
        BigDecimal currentPrice = instrumentsService.apiSymbolMatching(instruments.getApiSymbol());
        BigDecimal profitLoss = investments.stream().map(inv -> {
            BigDecimal cost = inv.getBuyPrice().multiply(inv.getQuantity());
            BigDecimal current = currentPrice.multiply(inv.getQuantity());
            return current.subtract(cost);
        }).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalValue = totalQuantity.multiply(currentPrice);

        return new DtoInvestmentSummaryResponse(instruments.getId(), instruments.getName(),
                instruments.getImageUrl(), instruments.getType(),
                totalQuantity.setScale(2, RoundingMode.HALF_UP),
                averageCost.setScale(2, RoundingMode.HALF_UP),
                currentPrice.setScale(2, RoundingMode.HALF_UP),
                profitLoss.setScale(2, RoundingMode.HALF_UP),
                totalValue.setScale(2, RoundingMode.HALF_UP)
        );
    }

    @Override
    @Transactional
    public void updateById(Long id, DtoInvestmentRequest dtoInvestmentRequest) {
        investmentRepository.updateById(id, dtoInvestmentRequest.instrumentsId(),
                dtoInvestmentRequest.quantity(), dtoInvestmentRequest.buyPrice());
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        int deletedRows = investmentRepository.deleteInvestmentSummary(id);
        if (deletedRows == 0) {
            throw new RuntimeException("Not Found Investment");
        }
    }

}
