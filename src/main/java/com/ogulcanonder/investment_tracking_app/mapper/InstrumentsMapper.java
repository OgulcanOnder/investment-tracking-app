package com.ogulcanonder.investment_tracking_app.mapper;

import com.ogulcanonder.investment_tracking_app.dto.request.DtoInstrumentsRequest;
import com.ogulcanonder.investment_tracking_app.dto.response.DtoInstrumentsResponse;
import com.ogulcanonder.investment_tracking_app.entity.Instruments;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.math.BigDecimal;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InstrumentsMapper {
    @Mapping(target = "price", constant = "0") // veya ignore da olabilir
    DtoInstrumentsResponse toCreateDto(Instruments instruments);
    @Mapping(target = "price", source = "price")
    DtoInstrumentsResponse toDto(Instruments instruments, BigDecimal price);
    Instruments toRequestEntity(DtoInstrumentsRequest dtoInstrumentsRequest);
}
