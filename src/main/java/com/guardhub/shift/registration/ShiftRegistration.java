package com.guardhub.shift.registration;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.guardhub.shift.Shift;
import com.guardhub.user.Guard;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "shift_registrations")
public class ShiftRegistration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long registrationId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "guard_id")
    private Guard guard;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JsonBackReference
    @JoinColumn(name = "shift_id")
    private Shift shift;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(nullable = false)
    private RegistrationStatus registrationStatus;

    public ShiftRegistration() {}

    public ShiftRegistration(Long registrationId, Guard guard, Shift shift, RegistrationStatus regStatus) {
        this.guard = guard;
        this.shift = shift;
        this.registrationId = registrationId;
        this.registrationStatus = regStatus;
    }

    public Long getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(Long registrationId) {
        this.registrationId = registrationId;
    }

    public Guard getGuard() {
        return guard;
    }

    public void setGuard(Guard guard) {
        this.guard = guard;
    }

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    public RegistrationStatus getRegistrationStatus() {
        return registrationStatus;
    }

    public void setRegistrationStatus(RegistrationStatus regStatus) {
        this.registrationStatus = regStatus;
    }

    @Override
    public String toString() {
        return "ShiftRegistration{" +
                "registrationId=" + registrationId +
                ", guard=" + (guard != null ? guard.getName() : "null") +
                ", shift=" + (shift != null ? shift.getShiftId() : "null") +
                ", registrationStatus=" + registrationStatus +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShiftRegistration that)) return false;
        return registrationId != null && registrationId.equals(that.registrationId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
