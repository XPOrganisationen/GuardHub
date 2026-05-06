package com.guardhub.shift.registration;

import jakarta.persistence.*;

@Entity
public class ShiftRegistration {
/*@ManyToMany
@JoinTable(name = "Shift_Guard")
 */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long registrationId;

    private Long guardId;
    private Long shiftId;

    @Enumerated(EnumType.STRING)
    private RegistrationStatus regStatus;

    public ShiftRegistration(Long registrationId, Long guardId, Long shiftId, RegistrationStatus regStatus) {
        this.registrationId = registrationId;
        this.guardId = guardId;
        this.shiftId = shiftId;
        this.regStatus = regStatus;
    }

    public ShiftRegistration() {}

    public Long getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(Long registrationId) {
        this.registrationId = registrationId;
    }

    public Long getGuardId() {
        return guardId;
    }

    public void setGuardId(Long guardId) {
        this.guardId = guardId;
    }

    public Long getShiftId() {
        return shiftId;
    }

    public void setShiftId(Long shiftId) {
        this.shiftId = shiftId;
    }

    public RegistrationStatus getRegStatus() {
        return regStatus;
    }

    public void setRegStatus(RegistrationStatus regStatus) {
        this.regStatus = regStatus;
    }
}
