package com.ogulcanonder.investment_tracking_app.repository;

import com.ogulcanonder.investment_tracking_app.entity.Debt;
import com.ogulcanonder.investment_tracking_app.enums.DebtType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface DebtRepository extends JpaRepository<Debt, Long> {

    List<Debt> findByUserId(Long userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Debt d WHERE d.id=:id AND d.user.id=:userId")
    int deleteDebtById(@Param("id") Long id, @Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE Debt d SET d.debtType=:debtType, d.description=:description, d.amount=:amount " +
            "WHERE d.id=:id AND d.user.id=:userId")
    int updateDebtById(@Param("id") Long id, @Param("debtType") DebtType debtType,
                        @Param("description") String description,
                        @Param("amount") BigDecimal amount, @Param("userId") Long userId);
}
