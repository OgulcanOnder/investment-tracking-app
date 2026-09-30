package com.ogulcanonder.investment_tracking_app.mapper;

import com.ogulcanonder.investment_tracking_app.dto.response.DtoInstrumentsResponse;
import com.ogulcanonder.investment_tracking_app.entity.Instruments;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InstrumentsMapperTest {

    private InstrumentsMapper instrumentsMapper = Mappers.getMapper(InstrumentsMapper.class);

    @DisplayName("should Instrument map DtoInstrumentsResponse and set price zero")
    @Test
    public void shouldSetPriceToZeroWhenInstrumentMapDtoInstrumentResponse() {
        Instruments instruments = Instruments.builder()
                .id(1L)
                .name("instruments name")
                .imageUrl("http://instrument-name.com")
                .apiSymbol("instrument api symbol")
                .type("type")
                .build();

        DtoInstrumentsResponse expectedResult = DtoInstrumentsResponse.builder()
                .id(1L)
                .name("instruments name")
                .imageUrl("http://instrument-name.com")
                .type("type")
                .price(BigDecimal.ZERO)
                .build();

        DtoInstrumentsResponse result = instrumentsMapper.toCreateDto(instruments);

        assertEquals(expectedResult, result);

    }

    @DisplayName("should specified price variable mapping price and instrument mapping DtoInstrumentResponse")
    @Test
    public void shouldMapVariablePriceWhenInstrumentMapDtoInstrumentResponse() {
        BigDecimal price = BigDecimal.valueOf(10.00);
        Instruments instruments = Instruments.builder()
                .id(1L)
                .name("instruments name")
                .imageUrl("http://instrument-name.com")
                .apiSymbol("instrument api symbol")
                .type("type")
                .build();

        DtoInstrumentsResponse expectedResult = DtoInstrumentsResponse.builder()
                .id(1L)
                .name("instruments name")
                .imageUrl("http://instrument-name.com")
                .type("type")
                .price(price)
                .build();

        DtoInstrumentsResponse result = instrumentsMapper.toDto(instruments, price);

        assertEquals(expectedResult, result);
    }

    @DisplayName("should null specified price variable mapping price and instrument mapping DtoInstrumentResponse")
    @Test
    public void shouldMapVariablePriceNullWhenInstrumentMapDtoInstrumentResponse() {
        BigDecimal price = null;

        Instruments instruments = Instruments.builder()
                .id(1L)
                .name("instruments name")
                .imageUrl("http://instrument-name.com")
                .apiSymbol("instrument api symbol")
                .type("type")
                .build();

        DtoInstrumentsResponse expectedResult = DtoInstrumentsResponse.builder()
                .id(1L)
                .name("instruments name")
                .imageUrl("http://instrument-name.com")
                .type("type")
                .price(price)
                .build();

        DtoInstrumentsResponse result = instrumentsMapper.toDto(instruments, price);

        assertEquals(expectedResult, result);
    }

}
