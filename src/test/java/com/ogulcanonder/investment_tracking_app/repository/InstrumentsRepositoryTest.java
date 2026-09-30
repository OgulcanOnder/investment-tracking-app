package com.ogulcanonder.investment_tracking_app.repository;

import com.ogulcanonder.investment_tracking_app.entity.Instruments;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.TestConstructor;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class InstrumentsRepositoryTest {
    private final InstrumentsRepository instrumentsRepository;
    private final TestEntityManager testEntityManager;

    public InstrumentsRepositoryTest(InstrumentsRepository instrumentsRepository, TestEntityManager testEntityManager) {
        this.instrumentsRepository = instrumentsRepository;
        this.testEntityManager = testEntityManager;
    }

    @DisplayName("should update instrument with instrument by id when updated instrument")
    @Test
    public void shouldUpdateInstrumentWithInstrumentByIdWhenUpdatedInstrument() {
        Instruments instruments = Instruments.builder()
                .name("instrument name")
                .imageUrl("http://instrument-name.com")
                .apiSymbol("instrument api symbol")
                .type("type")
                .build();
        Long id = (Long) testEntityManager.persistAndGetId(instruments);
        testEntityManager.flush();
        testEntityManager.clear();
        instrumentsRepository.updateById(id, "update instrument name", "http://update-instrument-name.com",
                "update instrument api symbol", "type");
        testEntityManager.flush();
        Instruments updatedInstrument = testEntityManager.find(Instruments.class, id);
        assertThat(updatedInstrument.getName()).isEqualTo("update instrument name");
        assertThat(updatedInstrument.getImageUrl()).isEqualTo("http://update-instrument-name.com");
        assertThat(updatedInstrument.getApiSymbol()).isEqualTo("update instrument api symbol");
        assertThat(updatedInstrument.getType()).isEqualTo("type");
    }

    @DisplayName("should return 1 delete row with delete instrument by id when deleted instrument")
    @Test
    public void shouldReturnOneDeleteInstrumentWithInstrumentByIdWhenDeletedInstrument() {
        Instruments instruments = Instruments.builder()
                .name("instrument name")
                .imageUrl("http://instrument-name.com")
                .apiSymbol("instrument api symbol")
                .type("type")
                .build();
        Long id = (Long) testEntityManager.persistAndGetId(instruments);
        testEntityManager.flush();
        testEntityManager.clear();
        int deletedRows = instrumentsRepository.deleteInstrumentsById(id);
        testEntityManager.flush();
        Instruments deletedInstrument = testEntityManager.find(Instruments.class, id);
        assertThat(deletedRows).isEqualTo(1);
        assertThat(deletedInstrument).isNull();
    }

    @DisplayName("should return 0 delete row with delete instrument by id when not deleted instrument ")
    @Test
    public void shouldReturnZeroDeleteInstrumentWithInstrumentByIdWhenNotDeletedInstrument() {
        Long id = 1L;
        int deletedRows = instrumentsRepository.deleteInstrumentsById(id);
        assertThat(deletedRows).isZero();
    }

    @DisplayName("should return 0 delete row with delete null instrument by id when not deleted instrument ")
    @Test
    public void shouldReturnZeroDeleteInstrumentWithNullInstrumentByIdWhenNotDeletedInstrument() {
        Long id = null;
        int deletedRows = instrumentsRepository.deleteInstrumentsById(id);
        assertThat(deletedRows).isZero();
    }
}
