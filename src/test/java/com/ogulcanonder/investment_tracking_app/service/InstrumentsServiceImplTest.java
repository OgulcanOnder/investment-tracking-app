package com.ogulcanonder.investment_tracking_app.service;

import com.ogulcanonder.investment_tracking_app.dto.request.DtoInstrumentsRequest;
import com.ogulcanonder.investment_tracking_app.dto.response.DtoInstrumentsResponse;
import com.ogulcanonder.investment_tracking_app.entity.Instruments;
import com.ogulcanonder.investment_tracking_app.exception.ResourceNotFoundException;
import com.ogulcanonder.investment_tracking_app.mapper.InstrumentsMapper;
import com.ogulcanonder.investment_tracking_app.repository.InstrumentsRepository;
import com.ogulcanonder.investment_tracking_app.service.impl.InstrumentsServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class InstrumentsServiceImplTest {

    private InstrumentsServiceImpl instrumentsServiceImpl;
    private InstrumentsRepository instrumentsRepository;
    private InstrumentsMapper instrumentsMapper;
    private InstrumentPriceService instrumentPriceService;

    @BeforeEach
    public void setUp() {
        instrumentsRepository = Mockito.mock(InstrumentsRepository.class);
        instrumentsMapper = Mockito.mock(InstrumentsMapper.class);
        instrumentPriceService = Mockito.mock(InstrumentPriceService.class);
        instrumentsServiceImpl = new InstrumentsServiceImpl(instrumentsRepository, instrumentsMapper, instrumentPriceService);
    }


    @DisplayName("should DtoInstrumentsRequest create and return DtoInstrumentsResponse with detailed instrument when instrument name and api symbol unique ")
    @Test
    public void shouldCreateAndReturnDtoInstrumentsResponseWithInstrumentWhenInstrumentNameAndApiSymbolUnique() {
        DtoInstrumentsRequest dtoInstrumentsRequest = DtoInstrumentsRequest.builder()
                .name("instrument name")
                .imageUrl("http://instrument-url.com")
                .apiSymbol("api symbol")
                .type("type")
                .build();
        Instruments instruments = new Instruments(1L, dtoInstrumentsRequest.name(), dtoInstrumentsRequest.imageUrl(), dtoInstrumentsRequest.apiSymbol(), dtoInstrumentsRequest.type());
        DtoInstrumentsResponse expectedResult = new DtoInstrumentsResponse(1L, instruments.getName(), instruments.getImageUrl(), instruments.getType(), BigDecimal.valueOf(100.00));

        Mockito.when(instrumentsMapper.toRequestEntity(dtoInstrumentsRequest)).thenReturn(instruments);
        Mockito.when(instrumentsRepository.save(instruments)).thenReturn(instruments);
        Mockito.when(instrumentsMapper.toCreateDto(instruments)).thenReturn(expectedResult);

        DtoInstrumentsResponse result = instrumentsServiceImpl.create(dtoInstrumentsRequest);

        assertEquals(expectedResult, result);

        Mockito.verify(instrumentsRepository, Mockito.times(1)).save(instruments);
        Mockito.verify(instrumentsMapper, Mockito.times(1)).toRequestEntity(dtoInstrumentsRequest);
        Mockito.verify(instrumentsMapper, Mockito.times(1)).toCreateDto(instruments);

    }

    @DisplayName("should throw DataIntegrityViolationException when instrument name and api symbol not unique ")
    @Test
    public void shouldThrowDataIntegrityViolationExceptionWhenInstrumentNameAndApiSymbolNotUnique() {
        DtoInstrumentsRequest dtoInstrumentsRequest = DtoInstrumentsRequest.builder()
                .name("instrument name")
                .imageUrl("http://instrument-url.com")
                .apiSymbol("api symbol")
                .type("type")
                .build();
        Instruments instruments = new Instruments(1L, dtoInstrumentsRequest.name(), dtoInstrumentsRequest.imageUrl(), dtoInstrumentsRequest.apiSymbol(), dtoInstrumentsRequest.type());

        Mockito.when(instrumentsMapper.toRequestEntity(dtoInstrumentsRequest)).thenReturn(instruments);
        Mockito.when(instrumentsRepository.save(instruments)).thenThrow(new DataIntegrityViolationException("DataIntegrityViolationException"));


        assertThrows(DataIntegrityViolationException.class, () -> instrumentsServiceImpl.create(dtoInstrumentsRequest));

        Mockito.verify(instrumentsRepository, Mockito.times(1)).save(instruments);
        Mockito.verify(instrumentsMapper, Mockito.times(1)).toRequestEntity(dtoInstrumentsRequest);

    }

    @DisplayName("should throw RuntimeException when instrument name and api symbol unique ")
    @Test
    public void shouldThrowRuntimeExceptionWhenInstrumentNameAndApiSymbolUnique() {
        DtoInstrumentsRequest dtoInstrumentsRequest = DtoInstrumentsRequest.builder()
                .name("instrument name")
                .imageUrl("http://instrument-url.com")
                .apiSymbol("api symbol")
                .type("type")
                .build();
        Instruments instruments = new Instruments(1L, dtoInstrumentsRequest.name(), dtoInstrumentsRequest.imageUrl(), dtoInstrumentsRequest.apiSymbol(), dtoInstrumentsRequest.type());

        Mockito.when(instrumentsMapper.toRequestEntity(dtoInstrumentsRequest)).thenReturn(instruments);
        Mockito.when(instrumentsRepository.save(instruments)).thenThrow(new RuntimeException("RuntimeException"));


        assertThrows(RuntimeException.class, () -> instrumentsServiceImpl.create(dtoInstrumentsRequest));

        Mockito.verify(instrumentsRepository, Mockito.times(1)).save(instruments);
        Mockito.verify(instrumentsMapper, Mockito.times(1)).toRequestEntity(dtoInstrumentsRequest);

    }

    @DisplayName("should return DtoInstrumentsResponse with get all instruments when instruments exists ")
    @Test
    public void shouldReturnListDtoInstrumentResponseWithGetAllInstrumentsWhenInstrumentsExists() {
        Instruments instruments = Instruments.builder()
                .id(1L)
                .name("instrument name")
                .imageUrl("http://instrument-url.com")
                .apiSymbol("api symbol")
                .type("type")
                .build();
        Instruments instruments2 = Instruments.builder()
                .id(2L)
                .name("instrument name 2")
                .imageUrl("http://instrument-url.com")
                .apiSymbol("api symbol 2")
                .type("type")
                .build();
        List<Instruments> instrumentList = Arrays.asList(instruments, instruments2);

        DtoInstrumentsResponse dtoInstrumentsResponse = DtoInstrumentsResponse.builder()
                .id(1L)
                .name("instrument name")
                .imageUrl("http://instrument-url.com")
                .type("type").
                price(BigDecimal.valueOf(100.00)).
                build();
        DtoInstrumentsResponse dtoInstrumentsResponse2 = DtoInstrumentsResponse.builder()
                .id(2L)
                .name("instrument name 2")
                .imageUrl("http://instrument-url.com")
                .type("type").
                price(BigDecimal.valueOf(100.00)).
                build();

        List<DtoInstrumentsResponse> expectedResult = Arrays.asList(dtoInstrumentsResponse, dtoInstrumentsResponse2);

        Mockito.when(instrumentsRepository.findAll()).thenReturn(instrumentList);
        Mockito.when(instrumentPriceService.getPrice(instruments.getApiSymbol())).thenReturn(BigDecimal.valueOf(100.00));
        Mockito.when(instrumentPriceService.getPrice(instruments2.getApiSymbol())).thenReturn(BigDecimal.valueOf(100.00));
        Mockito.when(instrumentsMapper.toDto(instruments, BigDecimal.valueOf(100.00))).thenReturn(dtoInstrumentsResponse);
        Mockito.when(instrumentsMapper.toDto(instruments2, BigDecimal.valueOf(100.00))).thenReturn(dtoInstrumentsResponse2);

        List<DtoInstrumentsResponse> result = instrumentsServiceImpl.getAll();
        assertEquals(expectedResult, result);

        Mockito.verify(instrumentsRepository, Mockito.times(1)).findAll();
        Mockito.verify(instrumentPriceService, Mockito.times(2)).getPrice(Mockito.any());
        Mockito.verify(instrumentsMapper, Mockito.times(2)).toDto(Mockito.any(), Mockito.any());

    }

    @DisplayName("should return empty instruments list when no instruments exists ")
    @Test
    public void shouldReturnListDtoInstrumentResponseWithGetAllInstrumentsWhenNoInstrumentsExists() {

        Mockito.when(instrumentsRepository.findAll()).thenReturn(Collections.emptyList());

        List<DtoInstrumentsResponse> result = instrumentsServiceImpl.getAll();
        assertTrue(result.isEmpty());

        Mockito.verify(instrumentsRepository, Mockito.times(1)).findAll();
        Mockito.verify(instrumentPriceService, Mockito.times(0)).getPrice(Mockito.any());
    }

    @DisplayName("should return DtoInstrumentsResponse with instrument when instrument by id ")
    @Test
    public void shouldReturnDtoInstrumentsResponseWithInstrumentWhenInstrumentById() {
        Instruments instruments = Instruments.builder()
                .id(1L)
                .name("instrument name")
                .imageUrl("http://instrument-url.com")
                .apiSymbol("api symbol")
                .type("type")
                .build();

        DtoInstrumentsResponse dtoInstrumentsResponse = DtoInstrumentsResponse.builder()
                .id(1L)
                .name("instrument name")
                .imageUrl("http://instrument-url.com")
                .type("type").
                price(BigDecimal.valueOf(100.00)).
                build();


        Mockito.when(instrumentsRepository.findById(instruments.getId())).thenReturn(Optional.of(instruments));
        Mockito.when(instrumentPriceService.getPrice(instruments.getApiSymbol())).thenReturn(BigDecimal.valueOf(100.00));
        Mockito.when(instrumentsMapper.toDto(instruments, BigDecimal.valueOf(100.00))).thenReturn(dtoInstrumentsResponse);


        DtoInstrumentsResponse result = instrumentsServiceImpl.getInstrumentsById(instruments.getId());
        assertEquals(dtoInstrumentsResponse, result);

        Mockito.verify(instrumentsRepository, Mockito.times(1)).findById(instruments.getId());
        Mockito.verify(instrumentPriceService, Mockito.times(1)).getPrice(instruments.getApiSymbol());
        Mockito.verify(instrumentsMapper, Mockito.times(1)).toDto(Mockito.any(), Mockito.any());

    }

    @DisplayName("should throw ResourceNotFoundException when instrument by id ")
    @Test
    public void shouldThrowResourceNotFoundExceptionWhenInstrumentById() {

        Mockito.when(instrumentsRepository.findById(1L)).thenThrow(new ResourceNotFoundException("ResourceNotFoundException"));

        assertThrows(ResourceNotFoundException.class, () -> instrumentsServiceImpl.getInstrumentsById(1L));

        Mockito.verify(instrumentsRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(instrumentPriceService, Mockito.times(0)).getPrice(Mockito.any());
        Mockito.verify(instrumentsMapper, Mockito.times(0)).toDto(Mockito.any(), Mockito.any());

    }

    @DisplayName("should return Instrument  when instrument by id ")
    @Test
    public void shouldReturnInstrumentsWhenInstrumentById() {
        Instruments instruments = Instruments.builder()
                .id(1L)
                .name("instrument name")
                .imageUrl("http://instrument-url.com")
                .apiSymbol("api symbol")
                .type("type")
                .build();


        Mockito.when(instrumentsRepository.findById(instruments.getId())).thenReturn(Optional.of(instruments));

        Instruments result = instrumentsServiceImpl.getInstrumentsEntityById(instruments.getId());

        assertEquals(instruments, result);

        Mockito.verify(instrumentsRepository, Mockito.times(1)).findById(instruments.getId());

    }

    @DisplayName("should throw ResourceNotFoundException when instrument by id")
    @Test
    public void shouldThrowResourceNotFoundExceptionWhenInstrumentsById() {

        Mockito.when(instrumentsRepository.findById(1L)).thenThrow(new ResourceNotFoundException("ResourceNotFoundException"));
        assertThrows(ResourceNotFoundException.class, () -> instrumentsServiceImpl.getInstrumentsEntityById(1L));

        Mockito.verify(instrumentsRepository, Mockito.times(1)).findById(1L);
    }

    @DisplayName("should call the updateById method in InstrumentRepository for updateById method in InstrumentsServiceImpl")
    @Test
    public void shouldUpdateInstrumentWithInstrumentByIdWhenUpdatedInstrument() {
        Long id = 1L;
        DtoInstrumentsRequest dtoInstrumentsRequest = DtoInstrumentsRequest.builder()
                .name("instrument name")
                .imageUrl("http://intrument-name.com")
                .apiSymbol("instrument api symbol")
                .type("type")
                .build();

        instrumentsServiceImpl.updateById(id, dtoInstrumentsRequest);

        Mockito.verify(instrumentsRepository, Mockito.times(1)).updateById(id, dtoInstrumentsRequest.name(),
                dtoInstrumentsRequest.imageUrl(), dtoInstrumentsRequest.apiSymbol(), dtoInstrumentsRequest.type());
    }

    @DisplayName("should throw DataIntegrityViolationException for not unique updated instrument ")
    @Test
    public void shouldThrowDataIntegrityViolationExceptionWhenNotUpdatedInstrument() {
        Long id = 1L;
        DtoInstrumentsRequest dtoInstrumentsRequest = DtoInstrumentsRequest.builder()
                .name("instrument name")
                .imageUrl("http://intrument-name.com")
                .apiSymbol("instrument api symbol")
                .type("type")
                .build();

        Mockito.doThrow(new DataIntegrityViolationException("DataIntegrityViolationException"))
                .when(instrumentsRepository).updateById(id, dtoInstrumentsRequest.name(), dtoInstrumentsRequest.imageUrl(),
                        dtoInstrumentsRequest.apiSymbol(), dtoInstrumentsRequest.type());

        assertThrows(DataIntegrityViolationException.class, () -> instrumentsServiceImpl.updateById(id, dtoInstrumentsRequest));
        Mockito.verify(instrumentsRepository, Mockito.times(1)).updateById(id, dtoInstrumentsRequest.name(), dtoInstrumentsRequest.imageUrl(),
                dtoInstrumentsRequest.apiSymbol(), dtoInstrumentsRequest.type());
    }

    @DisplayName("should throw RuntimeException when not updated instrument")
    @Test
    public void shouldThrowRuntimeExceptionWhenNotUpdatedInstrument() {
        Long id = 1L;
        DtoInstrumentsRequest dtoInstrumentsRequest = DtoInstrumentsRequest.builder()
                .name("instrument name")
                .imageUrl("http://intrument-name.com")
                .apiSymbol("instrument api symbol")
                .type("type")
                .build();

        Mockito.doThrow(new RuntimeException("Exception"))
                .when(instrumentsRepository).updateById(id, dtoInstrumentsRequest.name(), dtoInstrumentsRequest.imageUrl(),
                        dtoInstrumentsRequest.apiSymbol(), dtoInstrumentsRequest.type());

        assertThrows(RuntimeException.class, () -> instrumentsServiceImpl.updateById(id, dtoInstrumentsRequest));
        Mockito.verify(instrumentsRepository, Mockito.times(1)).updateById(id, dtoInstrumentsRequest.name(), dtoInstrumentsRequest.imageUrl(),
                dtoInstrumentsRequest.apiSymbol(), dtoInstrumentsRequest.type());
    }

    @DisplayName("should deleted instrument with instrument by id when deleted instrument and return delete rows number")
    @Test
    public void shouldReturnDeletedInstrumentWithInstrumentByIdWhenDeletedInstrument() {
        Long id = 1L;
        Mockito.when(instrumentsRepository.deleteInstrumentsById(id)).thenReturn(1);
        instrumentsServiceImpl.deleteById(id);
        Mockito.verify(instrumentsRepository, Mockito.times(1)).deleteInstrumentsById(id);
    }

    @DisplayName("should deleted instrument with instrument by id but not found instrument ")
    @Test
    public void shouldThrowResourceNotFoundExceptionWhenNotFoundInstrument() {
        Long id = 1L;
        Mockito.when(instrumentsRepository.deleteInstrumentsById(id)).thenReturn(0);
        assertThrows(ResourceNotFoundException.class, () -> instrumentsServiceImpl.deleteById(id));
        Mockito.verify(instrumentsRepository, Mockito.times(1)).deleteInstrumentsById(id);
    }


    @AfterEach()
    public void tearDown() {
    }
}
