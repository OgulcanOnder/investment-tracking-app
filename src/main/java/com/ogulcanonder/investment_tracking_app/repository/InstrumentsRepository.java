package com.ogulcanonder.investment_tracking_app.repository;

import com.ogulcanonder.investment_tracking_app.entity.Instruments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface InstrumentsRepository extends JpaRepository<Instruments, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE Instruments i SET i.name=?2, i.imageUrl=?3, i.apiSymbol=?4, i.type=?5 WHERE i.id=?1")
    void updateById(Long id, String name, String imageUrl, String apiSymbol, String type);


    @Modifying
    @Transactional
    @Query("DELETE FROM Instruments i WHERE i.id=:id")
    int deleteInstrumentsById(Long id);
}
