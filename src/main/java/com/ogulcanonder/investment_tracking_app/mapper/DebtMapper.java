package com.ogulcanonder.investment_tracking_app.mapper;

import com.ogulcanonder.investment_tracking_app.dto.response.DtoDebtResponse;
import com.ogulcanonder.investment_tracking_app.entity.Debt;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DebtMapper {
    DtoDebtResponse toDto(Debt debt);
}
