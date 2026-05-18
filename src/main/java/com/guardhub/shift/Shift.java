package com.guardhub.shift;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.guardhub.client.Client;
import com.guardhub.shift.registration.ShiftRegistration;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "shifts")
public class Shift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shiftId;

    @NotNull
    @NotBlank
    @Column(nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id")
    private Client client;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime shiftStart;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime shiftEnd;
    //    private List<Certificate> requiredCertificates;

    @NotNull
    @Column(name = "required_guards")
    private int requiredGuardAmount;

    @NotNull
    @NotBlank
    @Column(nullable = false)
    private String description;

    @OneToMany(mappedBy = "shift", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ShiftRegistration> shiftRegistrations = new ArrayList<>();

    private boolean foodServed;
    private String parkingInstructions;

    public Shift() {}

    public Shift(Long shiftId, String title, Client client, String description, int requiredGuardAmount, LocalDateTime shiftStart, LocalDateTime shiftEnd, boolean foodServed, String parkingInstructions) {
        this.client = client;
        this.description = description;
        this.requiredGuardAmount = requiredGuardAmount;
        this.shiftEnd = shiftEnd;
        this.shiftId = shiftId;
        this.foodServed = foodServed;
        this.parkingInstructions = parkingInstructions;
        this.shiftRegistrations = List.of();
        this.shiftStart = shiftStart;
        this.title = title;
    }

    public Long getShiftId() {
        return shiftId;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isFoodServed() {
        return foodServed;
    }

    public void setFoodServed(boolean foodServed) {
        this.foodServed = foodServed;
    }

    public String getParkingInstructions() {
        return parkingInstructions;
    }

    public void setParkingInstructions(String parkingInstructions) {
        this.parkingInstructions = parkingInstructions;
    }
}
