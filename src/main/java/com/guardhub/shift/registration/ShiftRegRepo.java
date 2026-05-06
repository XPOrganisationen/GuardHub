package com.guardhub.shift.registration;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ShiftRegRepo extends JpaRepository <ShiftRegistration, Long> {

}
