package com.guardhub.shift;

import java.time.LocalDateTime;

public class Shift {
    private final long shiftId;
    //    private final Client client;
    private LocalDateTime shiftStart;
    private LocalDateTime shiftEnd;
    //    private List<Certificate> requiredCertificates;
    private int requiredGuardAmount;
    //    private List<Guard> assignedGuards;
    private ShiftStatus shiftStatus;
    private String description;

    public enum ShiftStatus {AVAILABLE, ASSIGNED, COMPLETED, CANCELLED}

    public Shift(long shiftId,/* Client client,*/ LocalDateTime shiftStart, LocalDateTime shiftEnd, /*List<Certificate> requiredCertificates,*/
                 int requiredGuardAmount, /*List<Guard> assignedGuards,*/ShiftStatus shiftStatus, String description) {
        this.shiftId = shiftId;
//        this.client = client;
        this.shiftStart = shiftStart;
        this.shiftEnd = shiftEnd;
//        this.requiredCertificates = requiredCertificates;
        this.requiredGuardAmount = requiredGuardAmount;
//        this.assignedGuards = assignedGuards;
        this.shiftStatus = shiftStatus;
        this.description = description;
    }

    public long getShiftId() {
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

//    public List<Guard> getAssignedGuards() {
//        return assignedGuards;
//    }

    // TODO: Update shift status when assigning and unassigning guards
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
