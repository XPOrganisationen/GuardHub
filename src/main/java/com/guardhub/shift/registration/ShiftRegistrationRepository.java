package com.guardhub.shift.registration;

import com.guardhub.shift.Shift;
import com.guardhub.user.Guard;
import com.guardhub.user.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ShiftRegistrationRepository extends JpaRepository <ShiftRegistration, Long> {
    Optional<ShiftRegistration> findByGuardAndShift(Guard guard, Shift shift);

    List<ShiftRegistration> findByShiftAndRegistrationStatus(Shift shift, RegistrationStatus regStatus);

    @Query("SELECT r.guard.name FROM ShiftRegistration r WHERE r.shift.shiftId = :shiftId AND r.registrationStatus = 'APPROVED'")
    List<String> findAcceptedGuardNamesByShiftId(@Param("shiftId") Long shiftId);

    @Query("SELECT COUNT(*) > 0 FROM ShiftRegistration r WHERE r.shift.shiftId = :shiftId AND r.guard.userId = :guardId AND r.registrationStatus NOT IN ('CANCELED', 'REJECTED')")
    Boolean guardHasRegistrationForShift(Long guardId, Long shiftId);

    @Modifying
    @Transactional
    @Query("DELETE FROM ShiftRegistration r WHERE r.shift.shiftId = :shiftId AND r.guard.userId = :guardId")
    void deleteByShiftIdAndGuardId(@Param("shiftId") Long shiftId, @Param("guardId") Long guardId);

    List<ShiftRegistration> findAllByGuard(Guard guard);

    @Query("SELECT r FROM ShiftRegistration r WHERE r.registrationStatus = :registrationStatus")
    List<ShiftRegistration> findByRegistrationStatus(@Param("registrationStatus") RegistrationStatus registrationStatus);

    @Query("SELECT COUNT(r) FROM ShiftRegistration r WHERE r.shift = :shift AND r.registrationStatus = 'APPROVED'")
    long countApprovedRegistrationsByShift(@Param("shift") Shift shift);

    @Query("SELECT r FROM ShiftRegistration r WHERE r.guard = :guard")
    List<ShiftRegistration> findActiveRegistrationsByGuard(@Param("guard") Guard guard);
}
