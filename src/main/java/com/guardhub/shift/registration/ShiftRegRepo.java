package com.guardhub.shift.registration;

import com.guardhub.shift.Shift;
import com.guardhub.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShiftRegRepo extends JpaRepository<ShiftRegistration, Long> {
    Optional<ShiftRegistration> findByGuardAndShift(User guard, Shift shift);

    List<ShiftRegistration> findByShiftAndRegStatus(Shift shift, RegistrationStatus regStatus);
}
