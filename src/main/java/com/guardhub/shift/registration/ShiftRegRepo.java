package com.guardhub.shift.registration;

import com.guardhub.shift.Shift;
import com.guardhub.user.Guard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ShiftRegRepo extends JpaRepository<ShiftRegistration, Long> {
    Optional<ShiftRegistration> findByGuardAndShift(Guard guard, Shift shift);

    List<ShiftRegistration> findByShiftAndRegistrationStatus(Shift shift, RegistrationStatus registrationStatus);

    List<ShiftRegistration> findByGuard(Guard guard);

    List<ShiftRegistration> findByGuardAndRegistrationStatus(Guard guard, RegistrationStatus registrationStatus);

    List<ShiftRegistration> findByRegistrationStatus(RegistrationStatus registrationStatus);

    @Query("SELECT sr FROM ShiftRegistration sr WHERE sr.shift = :shift AND sr.registrationStatus = 'APPROVED'")
    List<ShiftRegistration> findApprovedRegistrationsByShift(@Param("shift") Shift shift);

    @Query("SELECT sr FROM ShiftRegistration sr WHERE sr.guard = :guard AND sr.registrationStatus IN ('PENDING', 'APPROVED')")
    List<ShiftRegistration> findActiveRegistrationsByGuard(@Param("guard") Guard guard);

    @Query("SELECT COUNT(sr) FROM ShiftRegistration sr WHERE sr.shift = :shift AND sr.registrationStatus = 'APPROVED'")
    long countApprovedRegistrationsByShift(@Param("shift") Shift shift);
}
