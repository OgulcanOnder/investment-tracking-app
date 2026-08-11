package com.ogulcanonder.investment_tracking_app.repository;

import com.ogulcanonder.investment_tracking_app.entity.Investment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface InvestmentRepository extends JpaRepository<Investment, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE Investment i SET i.instruments.id=?2, i.quantity=?3, i.buyPrice=?4 WHERE i.id=?1")
    void updateById(Long id, Long instrumentsId, BigDecimal quantity, BigDecimal buyPrice);

    @Modifying
    @Transactional
    @Query("DELETE FROM Investment i WHERE i.id=:id")
    int deleteInvestmentSummary(Long id);

    @Query("SELECT i FROM Investment i JOIN FETCH i.instruments WHERE i.user.id=:userId")
    List<Investment>findByUserIdWithInstruments(@Param("userId") Long userId);
}
