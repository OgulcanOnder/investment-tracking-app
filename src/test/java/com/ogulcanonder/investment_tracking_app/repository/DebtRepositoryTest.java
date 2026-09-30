package com.ogulcanonder.investment_tracking_app.repository;

import com.ogulcanonder.investment_tracking_app.entity.Debt;
import com.ogulcanonder.investment_tracking_app.entity.User;
import com.ogulcanonder.investment_tracking_app.enums.DebtType;
import com.ogulcanonder.investment_tracking_app.roles.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.TestConstructor;

import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class DebtRepositoryTest {

    private final DebtRepository debtRepository;
    private final TestEntityManager testEntityManager;

    public DebtRepositoryTest(DebtRepository debtRepository, TestEntityManager testEntityManager) {
        this.debtRepository = debtRepository;
        this.testEntityManager = testEntityManager;
    }

    @DisplayName("should return 1 delete row with debt by id and user id when deleted debt")
    @Test
    public void shouldReturnOneDeleteDebtWithDebtByIdAndUserIdWhenDeletedDebt() {
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

        Debt debt = Debt.builder()
                .debtType(DebtType.PERSONAL_DEBT)
                .description("test description")
                .amount(BigDecimal.valueOf(100.000))
                .user(user)
                .build();

        Long debtId = (Long) testEntityManager.persistAndGetId(debt);

        testEntityManager.flush();
        testEntityManager.clear();
        int deletedRows = debtRepository.deleteDebtById(debtId, userId);
        testEntityManager.flush();
        Debt deletedDebt = testEntityManager.find(Debt.class, debtId);
        assertThat(deletedRows).isEqualTo(1);
        assertThat(deletedDebt).isNull();
    }

    @DisplayName("should return 0 delete row with not debt by id and user id when not deleted debt")
    @Test
    public void shouldReturnZeroDeleteDebtWithDebtByIdAndUserIdWhenDeletedDebt() {
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

        Long debtId = 1L;

        testEntityManager.flush();
        testEntityManager.clear();
        int deletedRows = debtRepository.deleteDebtById(debtId, userId);
        testEntityManager.flush();
        assertThat(deletedRows).isZero();
    }

    @DisplayName("should return 1 update row with debt by id and user id when updated debt")
    @Test
    public void shouldReturnOneUpdateDebtWithDebtByIdAndUserIdWhenUpdatedDebt() {
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

        Debt debt = Debt.builder()
                .debtType(DebtType.PERSONAL_DEBT)
                .description("test description")
                .amount(BigDecimal.valueOf(100.000))
                .user(user)
                .build();

        Long debtId = (Long) testEntityManager.persistAndGetId(debt);

        testEntityManager.flush();
        testEntityManager.clear();
        int updatedRows = debtRepository.updateDebtById(debtId,DebtType.BILL,"update test description",
                BigDecimal.valueOf(150.00),userId);
        testEntityManager.flush();
        Debt updatedDebt = testEntityManager.find(Debt.class, debtId);
        assertThat(updatedRows).isEqualTo(1);
        assertThat(updatedDebt).isNotNull();
    }

    @DisplayName("should return 0 update row with not debt by id and user id when not update debt")
    @Test
    public void shouldReturnZeroUpdateDebtWithNotDebtByIdAndUserIdWhenUpdatedDebt() {
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

        Long debtId = 1L;

        testEntityManager.flush();
        testEntityManager.clear();
        int updatedRows = debtRepository.updateDebtById(debtId,DebtType.BILL,"update test description",
                BigDecimal.valueOf(150.00),userId);
        testEntityManager.flush();
        assertThat(updatedRows).isEqualTo(0);

    }


}
