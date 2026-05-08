package com.guardhub.shift.registration;

import com.guardhub.shift.Shift;
import com.guardhub.user.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShiftRegService {

    private final ShiftRegRepo shiftRegRepo;

    public ShiftRegService(ShiftRegRepo shiftRegRepo) {
        this.shiftRegRepo = shiftRegRepo;
    }

    public List<ShiftRegistration> findAllRegistrations() {
        return shiftRegRepo.findAll();
    }

    public Optional<ShiftRegistration> findByRegId(Long registrationId) {
        return shiftRegRepo.findById(registrationId);
    }

    public ShiftRegistration addRegistration(ShiftRegistration shiftRegistration) {
        User guard = shiftRegistration.getGuard();
        Shift shift = shiftRegistration.getShift();

            // for a shift registration to go through, a guard must be assigned a shift that is existent.
        if (guard == null || shift == null) {
            throw new IllegalArgumentException("Guard and Shift must not be null");
        }
            // Limiting shifts to the guard role (An admin cannot sign up for shifts)
        if (!guard.isGuard()) {
            throw new IllegalArgumentException("User must be a guard to register for a shift");
        }
            // Making sure you cannot sign up for shift that's already taking place.
        if (shift.getShiftStart().isBefore(java.time.LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot register for a shift in the past");
        }
            // Double booking prevention:
        if (shiftRegRepo.findByGuardAndShift(guard, shift).isPresent()) {
            throw new IllegalArgumentException("Guard is already registered for this shift");
        }
            // By default, set shift registration to pending, until an admin has approved the selection(s) of guards.
        shiftRegistration.setRegStatus(RegistrationStatus.PENDING);
        return shiftRegRepo.save(shiftRegistration);
    }

    public ShiftRegistration updateRegistration(ShiftRegistration shiftRegistration) {
        return shiftRegRepo.save(shiftRegistration);
    }

    public void deleteRegistration(Long registrationId) {
        shiftRegRepo.deleteById(registrationId);
    }

    public List<ShiftRegistration> getPendingRegistrationsByShift(Shift shift) {
        return shiftRegRepo.findByShiftAndRegStatus(shift, RegistrationStatus.PENDING);
    }

        // Admin can approve a pending registration made by a guard.
    public ShiftRegistration approveRegistration(Long registrationId, User admin) {
        if (!admin.isAdmin()) {
            throw new IllegalArgumentException("Only admins can approve shift registrations");
        }

        ShiftRegistration registration = shiftRegRepo.findById(registrationId)
                .orElseThrow(() -> new IllegalArgumentException("No registration found with id: " + registrationId));

        registration.setRegStatus(RegistrationStatus.APPROVED);
        return shiftRegRepo.save(registration);
    }

        // Admin can reject a pending registration made by a guard.
    public ShiftRegistration rejectRegistration(Long registrationId, User admin) {
        if (!admin.isAdmin()) {
            throw new IllegalArgumentException("Only admins can reject shift registrations");
        }

        ShiftRegistration registration = shiftRegRepo.findById(registrationId)
                .orElseThrow(() -> new IllegalArgumentException("No registration found with id: " + registrationId));

        registration.setRegStatus(RegistrationStatus.REJECTED);
        return shiftRegRepo.save(registration);
    }

        // Gives an Admin the ability to forcefully remove a guard from a shift within the system, if it has been mutually agreed.
    public ShiftRegistration removeGuardFromShift(Long registrationId, User admin) {
        if (!admin.isAdmin()) {
            throw new IllegalArgumentException("Only admins can remove guards from shifts");
        }

        ShiftRegistration registration = shiftRegRepo.findById(registrationId)
                .orElseThrow(() -> new IllegalArgumentException("No registration found with id: " + registrationId));

        registration.setRegStatus(RegistrationStatus.CANCELLED);
        shiftRegRepo.save(registration);

        //TODO: be able to update shift status so when the amount of guards are < than the required for the shift, its status is set to available.
        return shiftRegRepo.save(registration);
    }

    // A Guard has the ability to remove himself from a shift (Only if the registration is pending).
    public ShiftRegistration cancelRegistration(Long registrationId, User guard) {
        ShiftRegistration registration = shiftRegRepo.findById(registrationId)
                .orElseThrow(() -> new IllegalArgumentException("No registration found with id: " + registrationId));

        if (!registration.getGuard().equals(guard)) {
            throw new IllegalArgumentException("Guards can only cancel their own registrations");
        }

        if (registration.getRegStatus() != RegistrationStatus.PENDING) {
            throw new IllegalArgumentException("Only pending registrations can be cancelled");
        }

        registration.setRegStatus(RegistrationStatus.CANCELLED);
        return shiftRegRepo.save(registration);
    }
}
