package com.ogulcanonder.investment_tracking_app.mapper;

import com.ogulcanonder.investment_tracking_app.dto.request.DtoInvestmentRequest;
import com.ogulcanonder.investment_tracking_app.dto.response.DtoInvestmentResponse;
import com.ogulcanonder.investment_tracking_app.entity.Instruments;
import com.ogulcanonder.investment_tracking_app.entity.Investment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)

public interface InvestmentMapper {
    // Request → Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "instruments", target = "instruments")
    Investment toEntity(DtoInvestmentRequest dtoInvestmentRequest, Instruments instruments);

    // Entity → Response
    DtoInvestmentResponse toDto(Investment investment);
}
