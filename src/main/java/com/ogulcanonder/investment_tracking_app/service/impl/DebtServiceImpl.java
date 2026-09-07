package com.ogulcanonder.investment_tracking_app.service.impl;

import com.ogulcanonder.investment_tracking_app.dto.request.DtoDebtRequest;
import com.ogulcanonder.investment_tracking_app.dto.response.DtoDebtResponse;
import com.ogulcanonder.investment_tracking_app.entity.Debt;
import com.ogulcanonder.investment_tracking_app.exception.ResourceNotFoundException;
import com.ogulcanonder.investment_tracking_app.mapper.DebtMapper;
import com.ogulcanonder.investment_tracking_app.repository.DebtRepository;
import com.ogulcanonder.investment_tracking_app.service.DebtService;
import com.ogulcanonder.investment_tracking_app.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class DebtServiceImpl implements DebtService {
    private final DebtRepository debtRepository;
    private final UserService userService;
    private final DebtMapper debtMapper;

    public DebtServiceImpl(DebtRepository debtRepository, UserService userService, DebtMapper debtMapper) {
        this.debtRepository = debtRepository;
        this.userService = userService;
        this.debtMapper = debtMapper;
    }

    @Transactional
    @Override
    public DtoDebtResponse create(DtoDebtRequest dtoDebtRequest) {
        Debt debt = Debt.builder()
                .debtType(dtoDebtRequest.debtType())
                .description(dtoDebtRequest.description())
                .amount(dtoDebtRequest.amount())
                .user(userService.getCurrentUser())
                .build();
        debtRepository.save(debt);
        return debtMapper.toDto(debt);
    }

    @Override
    public List<DtoDebtResponse> getAll() {
        Long userId = userService.getCurrentUser().getId();
        return debtRepository.findByUserId(userId).stream().map(debtMapper::toDto)
                .toList();

    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        Long userId = userService.getCurrentUser().getId();
        int deletedRows = debtRepository.deleteDebtById(id, userId);
        if (deletedRows == 0) {
            throw new ResourceNotFoundException("Not found debt");
        }
    }

    @Transactional
    @Override
    public void updateById(Long id, DtoDebtRequest dtoDebtRequest) {
        Long userId = userService.getCurrentUser().getId();
        int updateRows = debtRepository.updateDebtById(id, dtoDebtRequest.debtType(),
                dtoDebtRequest.description(), dtoDebtRequest.amount(), userId);
        if (updateRows == 0) {
            throw new ResourceNotFoundException("Not found debt");
        }
    }
}
