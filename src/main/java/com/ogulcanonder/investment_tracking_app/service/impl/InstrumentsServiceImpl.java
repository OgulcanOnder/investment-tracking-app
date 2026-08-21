package com.ogulcanonder.investment_tracking_app.service.impl;

import com.ogulcanonder.investment_tracking_app.dto.request.DtoInstrumentsRequest;
import com.ogulcanonder.investment_tracking_app.dto.response.DtoInstrumentsResponse;
import com.ogulcanonder.investment_tracking_app.entity.Instruments;
import com.ogulcanonder.investment_tracking_app.exception.ResourceNotFoundException;
import com.ogulcanonder.investment_tracking_app.mapper.InstrumentsMapper;
import com.ogulcanonder.investment_tracking_app.repository.InstrumentsRepository;
import com.ogulcanonder.investment_tracking_app.service.InstrumentPriceService;
import com.ogulcanonder.investment_tracking_app.service.InstrumentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InstrumentsServiceImpl implements InstrumentsService {

    private final InstrumentsRepository instrumentsRepository;
    private final InstrumentsMapper instrumentsMapper;
    private final InstrumentPriceService instrumentPriceService;

    @Override
    @Transactional
    public DtoInstrumentsResponse create(DtoInstrumentsRequest dtoInstrumentsRequest) {
        try {
            Instruments instruments = instrumentsMapper.toRequestEntity(dtoInstrumentsRequest);
            return instrumentsMapper.toCreateDto(instrumentsRepository.save(instruments));
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Stock name and stock code must be unique.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<DtoInstrumentsResponse> getAll() {
        return instrumentsRepository.findAll().stream()
                .map(inst -> {
                    BigDecimal price = instrumentPriceService.getPrice(inst.getApiSymbol());
                    return instrumentsMapper.toDto(inst, price);
                })
                .toList();
    }

    @Override
    public DtoInstrumentsResponse getInstrumentsById(Long id) {
        Instruments instruments = instrumentsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found Instruments"));
        BigDecimal price = instrumentPriceService.getPrice(instruments.getApiSymbol());
        return instrumentsMapper.toDto(instruments, price);
    }

    @Override
    public Instruments getInstrumentsEntityById(Long id) {
        return instrumentsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found Instruments"));
    }

    @Override
    @Transactional
    public void updateById(Long id, DtoInstrumentsRequest dtoInstrumentsRequest) {
        try {
            instrumentsRepository.updateById(id, dtoInstrumentsRequest.name(), dtoInstrumentsRequest.imageUrl(),
                    dtoInstrumentsRequest.apiSymbol(), dtoInstrumentsRequest.type());
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Instruments already exists.");
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred.");
        }
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        int deletedRows = instrumentsRepository.deleteInstrumentsById(id);
        if (deletedRows == 0) {
            throw new ResourceNotFoundException("Not Found Instruments");
        }
    }
}

