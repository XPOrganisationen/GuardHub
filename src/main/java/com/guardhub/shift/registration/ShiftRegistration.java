package com.guardhub.shift.registration;

import com.guardhub.shift.Shift;
import com.guardhub.user.User;
import jakarta.persistence.*;

@Entity
public class ShiftRegistration {
/*@ManyToMany
@JoinTable(name = "Shift_Guard")
 */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long registrationId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "guard_id")
    private User guard;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "shift_id")
    private Shift shift;

    @Enumerated(EnumType.STRING)
    private RegistrationStatus regStatus;

    public ShiftRegistration(Long registrationId, User guard, Shift shift, RegistrationStatus regStatus) {
        this.registrationId = registrationId;
        this.guard = guard;
        this.shift = shift;
        this.regStatus = regStatus;
    }

    public ShiftRegistration() {}

    public Long getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(Long registrationId) {
        this.registrationId = registrationId;
    }

    public User getGuard() {
        return guard;
    }

    public void setGuard(User guard) {
        this.guard = guard;
    }

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    public RegistrationStatus getRegStatus() {
        return regStatus;
    }

    public void setRegStatus(RegistrationStatus regStatus) {
        this.regStatus = regStatus;
    }
}
