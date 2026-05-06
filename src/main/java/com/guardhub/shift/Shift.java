package com.guardhub.shift;

import com.guardhub.shift.registration.ShiftRegistration;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Shift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shiftId;

    //    private final Client client;
    private LocalDateTime shiftStart;
    private LocalDateTime shiftEnd;
    //    private List<Certificate> requiredCertificates;
    private int requiredGuardAmount;

    @Enumerated(EnumType.STRING)
    private ShiftStatus shiftStatus;

    private String description;

    @OneToMany(mappedBy = "shift", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShiftRegistration> shiftRegistrations = new ArrayList<>();

    public enum ShiftStatus {AVAILABLE, ASSIGNED, COMPLETED, CANCELLED}

    public Shift() {}

    public Long getShiftId() {
        return shiftId;
    }

//    public Client getClient() {
//        return client;
//    }

    public LocalDateTime getShiftStart() {
        return shiftStart;
    }

    public void setShiftStart(LocalDateTime shiftStart) {
        this.shiftStart = shiftStart;
    }

    public LocalDateTime getShiftEnd() {
        return shiftEnd;
    }

    public void setShiftEnd(LocalDateTime shiftEnd) {
        this.shiftEnd = shiftEnd;
    }

    public int getRequiredGuardAmount() {
        return requiredGuardAmount;
    }

    public void setRequiredGuardAmount(int requiredGuardAmount) {
        this.requiredGuardAmount = requiredGuardAmount;
    }

//    public List<Certificate> getRequiredCertificates() {
//        return requiredCertificates;
//    }

//    public void addRequiredCertificate(Certificate certificate) {
//        requiredCertificates.add(certificate);
//    }

//    public boolean hasAnyRequiredCertificates() {
//        return !requiredCertificates.isEmpty();
//    }

    public List<ShiftRegistration> getRegistrations() {
        return shiftRegistrations;
    }

    // TODO: Update shift status when assigning and unassigning guards
    public void addRegistration(ShiftRegistration registration) {
        shiftRegistrations.add(registration);
//        registration.setShift(this);
    }

    public void removeRegistration(ShiftRegistration registration) {
//        registrations.remove(registration);
//        registration.setShift(null);
    }

//    public boolean assignGuard(Guard guard) {
//        if (!canAssignGuard) return false;
//        this.assignedGuards.add(guard);
//        return true;
//    }

//    public boolean canAssignGuard(Guard guard) {
//        if (shiftStatus != ShiftStatus.AVAILABLE) return false;
//        if (!guard.hasCertificates(requiredCertificates)) return false;
//        return true;
//    }

//    public void unassignGuard(Guard guard) {
//        this.assignedGuards.remove(guard);
//    }

//    public boolean isGuardAssigned(Guard guard) {
//        return assignedGuards.contains(guard);
//    }

    public ShiftStatus getShiftStatus() {
        return shiftStatus;
    }

    public void setShiftStatus(ShiftStatus shiftStatus) {
        this.shiftStatus = shiftStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
