package com.ogulcanonder.investment_tracking_app.repository;

import com.ogulcanonder.investment_tracking_app.entity.Instruments;
import com.ogulcanonder.investment_tracking_app.entity.Investment;
import com.ogulcanonder.investment_tracking_app.entity.User;
import com.ogulcanonder.investment_tracking_app.roles.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.TestConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class InvestmentRepositoryTest {

    private final InvestmentRepository investmentRepository;
    private final TestEntityManager testEntityManager;

    public InvestmentRepositoryTest(InvestmentRepository investmentRepository, TestEntityManager testEntityManager) {
        this.investmentRepository = investmentRepository;
        this.testEntityManager = testEntityManager;
    }

    @DisplayName("should return 1 update row with investment by id and user id when updated investment")
    @Test
    public void shouldReturnOneUpdateInvestmentWithInvestmentByIdAndUserIdWhenUpdatedInvestment() {
        User user = User.builder()
                .name("test name")
                .surname("test surname")
                .username("test username")
                .email("test email")
                .password("test password")
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .isEnabled(true)
                .authorities(Set.of(Role.ROLE_USER))
                .build();

        Long userId = (Long) testEntityManager.persistAndGetId(user);

        Instruments instrument = Instruments.builder()
                .name("instrument name")
                .imageUrl("http://instrument-name.com")
                .apiSymbol("instrument api symbol")
                .type("type")
                .build();
        Long instrumentId=(Long) testEntityManager.persistAndGetId(instrument);

        Investment investment = Investment.builder()
                .quantity(BigDecimal.valueOf(50))
                .buyPrice(BigDecimal.valueOf(100.00))
                .buyDate(LocalDateTime.now())
                .instruments(instrument)
                .user(user)
                .build();
        Long investmentId = (Long) testEntityManager.persistAndGetId(investment);
        testEntityManager.flush();
        testEntityManager.clear();

        int updatedRows = investmentRepository.updateById(investmentId,instrumentId,
                BigDecimal.valueOf(40),BigDecimal.valueOf(130.000),userId);
        testEntityManager.flush();
        Investment updatedDebt = testEntityManager.find(Investment.class, investmentId);
        assertThat(updatedRows).isEqualTo(1);
        assertThat(updatedDebt).isNotNull();
    }

    @DisplayName("should return 0 update row with not investment by id and user id when not update investment")
    @Test
    public void shouldReturnZeroUpdateInvestmentWithNotInvestmentByIdAndUserIdWhenNotUpdateInvestment() {
        User user = User.builder()
                .name("test name")
                .surname("test surname")
                .username("test username")
                .email("test email")
                .password("test password")
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .isEnabled(true)
                .authorities(Set.of(Role.ROLE_USER))
                .build();

        Long userId = (Long) testEntityManager.persistAndGetId(user);
        Long investmentId=1L;
        Long instrumentId=2l;

        int updatedRows = investmentRepository.updateById(investmentId,instrumentId,
                BigDecimal.valueOf(40),BigDecimal.valueOf(130.000),userId);
        testEntityManager.flush();
        assertThat(updatedRows).isZero();
    }

    @DisplayName("should return 1 delete row with investment by id and user id when deleted investment")
    @Test
    public void shouldReturnOneDeleteInvestmentSummaryWithInvestmentByIdAndUserIdWhenDeletedInvestment() {
        User user = User.builder()
                .name("test name")
                .surname("test surname")
                .username("test username")
                .email("test email")
                .password("test password")
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .isEnabled(true)
                .authorities(Set.of(Role.ROLE_USER))
                .build();

        Long userId = (Long) testEntityManager.persistAndGetId(user);

        Instruments instrument = Instruments.builder()
                .name("instrument name")
                .imageUrl("http://instrument-name.com")
                .apiSymbol("instrument api symbol")
                .type("type")
                .build();
        Long instrumentId=(Long) testEntityManager.persistAndGetId(instrument);

        Investment investment = Investment.builder()
                .quantity(BigDecimal.valueOf(50))
                .buyPrice(BigDecimal.valueOf(100.00))
                .buyDate(LocalDateTime.now())
                .instruments(instrument)
                .user(user)
                .build();
        testEntityManager.persist(investment);
        testEntityManager.flush();
        testEntityManager.clear();

        int deletedRows = investmentRepository.deleteInvestmentSummary(instrumentId, userId);
        testEntityManager.flush();
        assertThat(deletedRows).isEqualTo(1);
    }

    @DisplayName("should return 0 delete row with not investment by id and user id when not delete investment")
    @Test
    public void shouldReturnZeroDeleteInvestmentSummaryWithNotInvestmentByIdAndUserIdWhenNotDeleteInvestment() {
        User user = User.builder()
                .name("test name")
                .surname("test surname")
                .username("test username")
                .email("test email")
                .password("test password")
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .isEnabled(true)
                .authorities(Set.of(Role.ROLE_USER))
                .build();

        Long userId = (Long) testEntityManager.persistAndGetId(user);

        Long instrumentId=1L;

        testEntityManager.flush();
        testEntityManager.clear();

        int deletedRows = investmentRepository.deleteInvestmentSummary(instrumentId, userId);
        testEntityManager.flush();
        assertThat(deletedRows).isZero();

    }

    @DisplayName("should return list investment with user id when list instrument")
    @Test
    public void shouldReturnListInvestmentWithUserIdWhenListInstruments() {

        User user = User.builder()
                .name("test name")
                .surname("test surname")
                .username("test username")
                .email("test email")
                .password("test password")
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .isEnabled(true)
                .authorities(Set.of(Role.ROLE_USER))
                .build();

        Long userId = (Long) testEntityManager.persistAndGetId(user);

        Instruments instrument = Instruments.builder()
                .name("instrument name")
                .imageUrl("http://instrument-name.com")
                .apiSymbol("instrument api symbol")
                .type("type")
                .build();
        testEntityManager.persist(instrument);

        Investment investment = Investment.builder()
                .quantity(BigDecimal.valueOf(50))
                .buyPrice(BigDecimal.valueOf(100.00))
                .buyDate(LocalDateTime.now())
                .instruments(instrument)
                .user(user)
                .build();
        testEntityManager.persist(investment);
        testEntityManager.flush();

        List<Investment> investmentList=investmentRepository.findByUserIdWithInstruments(userId);
        assertThat(investmentList).hasSize(1);
        assertThat(investment).isEqualTo(investmentList.getFirst());
        testEntityManager.clear();
    }

    @DisplayName("should return not found list investment with user id when list instrument")
    @Test
    public void shouldReturnNotFoundListInvestmentWithUserIdWhenListInstruments() {
        Long wrongUserId=100L;

        User user = User.builder()
                .name("test name")
                .surname("test surname")
                .username("test username")
                .email("test email")
                .password("test password")
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .isEnabled(true)
                .authorities(Set.of(Role.ROLE_USER))
                .build();
        testEntityManager.persist(user);

        Instruments instrument = Instruments.builder()
                .name("instrument name")
                .imageUrl("http://instrument-name.com")
                .apiSymbol("instrument api symbol")
                .type("type")
                .build();
        testEntityManager.persist(instrument);

        Investment investment = Investment.builder()
                .quantity(BigDecimal.valueOf(50))
                .buyPrice(BigDecimal.valueOf(100.00))
                .buyDate(LocalDateTime.now())
                .instruments(instrument)
                .user(user)
                .build();
        testEntityManager.persist(investment);
        testEntityManager.flush();

        List<Investment> investmentList=investmentRepository.findByUserIdWithInstruments(wrongUserId);
        assertThat(investmentList).hasSize(0);
        testEntityManager.clear();
    }



}
