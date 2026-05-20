package com.guardhub.shift.registration;

import java.time.LocalDateTime;

public record ShiftRegistrationDTO(Long registrationId, LocalDateTime shiftStart, String clientName, String guardName, RegistrationStatus registrationStatus) {
    public static ShiftRegistrationDTO from(ShiftRegistration shiftRegistration) {
        return new ShiftRegistrationDTO(
                shiftRegistration.getRegistrationId(),
                shiftRegistration.getShift().getShiftStart(),
                shiftRegistration.getShift().getClient().getName(),
                shiftRegistration.getGuard().getName()
                , shiftRegistration.getRegistrationStatus()
        );
    }
}
