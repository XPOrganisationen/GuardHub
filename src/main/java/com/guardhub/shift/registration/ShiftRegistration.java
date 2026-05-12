package com.guardhub.shift.registration;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.guardhub.shift.Shift;
import com.guardhub.user.User;
import jakarta.persistence.*;

@Entity
@Table(name = "shift_registrations")
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
    @JsonBackReference
    @JoinColumn(name = "shift_id")
    private Shift shift;

    @Enumerated(EnumType.STRING)
    private RegistrationStatus registrationStatus;

    public ShiftRegistration(Long registrationId, User guard, Shift shift, RegistrationStatus registrationStatus) {
        this.registrationId = registrationId;
        this.guard = guard;
        this.shift = shift;
        this.registrationStatus = registrationStatus;
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

    public RegistrationStatus getRegistrationStatus() {
        return registrationStatus;
    }

    public void setRegistrationStatus(RegistrationStatus regStatus) {
        this.registrationStatus = regStatus;
    }
}
