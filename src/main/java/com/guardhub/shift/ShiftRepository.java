package com.guardhub.shift;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ShiftRepository extends JpaRepository<Shift, Long> {
    @Query("""
    SELECT new com.guardhub.shift.ShiftDTO(
        s.shiftId,
        s.title,
        s.shiftStart,
        s.shiftEnd,
        s.description,
        ct.cityName,
        s.requiredGuardAmount,
        SUM(CASE WHEN r.registrationStatus = 'APPROVED' THEN 1 ELSE 0 END)
    )
    FROM Shift s
    LEFT JOIN s.shiftRegistrations r
    LEFT JOIN s.client c
    LEFT JOIN c.city ct
    WHERE s.shiftStart >= :weekStart AND s.shiftStart < :weekEnd
    GROUP BY s.shiftId, s.title, s.shiftStart, s.shiftEnd, s.description, ct.cityName, s.requiredGuardAmount
    """)
    List<ShiftDTO> findAllForWeek(@Param("weekStart") LocalDateTime weekStart,
                                  @Param("weekEnd") LocalDateTime weekEnd);

    @Query("""
    SELECT new com.guardhub.shift.ShiftDTO(
        s.shiftId,
        s.title,
        s.shiftStart,
        s.shiftEnd,
        s.description,
        ct.cityName,
        s.requiredGuardAmount,
        SUM(CASE WHEN r.registrationStatus = 'APPROVED' THEN 1 ELSE 0 END)
    )
    FROM Shift s
    LEFT JOIN s.shiftRegistrations r
    LEFT JOIN s.client c
    LEFT JOIN c.city ct
    WHERE s.shiftStart >= :weekStart
    AND s.shiftStart < :weekEnd
    AND EXISTS (
    SELECT 1 FROM s.shiftRegistrations gr
    WHERE gr.guard.userId = :guardId
    )
    GROUP BY s.shiftId, s.title, s.shiftStart, s.shiftEnd, s.description, ct.cityName, s.requiredGuardAmount
    """)
    List<ShiftDTO> findAllForGuardForWeek(
            @Param("guardId") Long guardId,
            @Param("weekStart") LocalDateTime weekStart,
            @Param("weekEnd") LocalDateTime weekEnd
    );
}
