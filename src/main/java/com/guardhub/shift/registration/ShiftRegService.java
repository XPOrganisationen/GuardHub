package com.guardhub.shift.registration;

import com.guardhub.shift.Shift;
import com.guardhub.user.Admin;
import com.guardhub.user.Guard;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public Optional<ShiftRegistration> findByRegistrationId(Long registrationId) {
        return shiftRegRepo.findById(registrationId);
    }

    public ShiftRegistration getByRegistrationId(Long registrationId) {
        return shiftRegRepo.findById(registrationId)
                .orElseThrow(() -> new IllegalArgumentException("No registration found with id: " + registrationId));
    }

    public List<ShiftRegistration> findByGuard(Guard guard) {
        return shiftRegRepo.findByGuard(guard);
    }

    public List<ShiftRegistration> findByRegistrationStatus(RegistrationStatus registrationStatus) {
        return shiftRegRepo.findByRegistrationStatus(registrationStatus);
    }

    public long getApprovedGuardCountForShift(Shift shift) {
        return shiftRegRepo.countApprovedRegistrationsByShift(shift);
    }

    public List<ShiftRegistration> getActiveRegistrationsForGuard(Guard guard) {
        return shiftRegRepo.findActiveRegistrationsByGuard(guard);
    }

    public ShiftRegistration registerGuardForShift(Guard guard, Shift shift) {
        if (guard == null || shift == null) {
            throw new IllegalArgumentException("Guard and Shift must not be null");
        }

        if (shift.getShiftStart().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot register for a shift in the past");
        }

        if (shiftRegRepo.findByGuardAndShift(guard, shift).isPresent()) {
            throw new IllegalArgumentException("Guard is already registered for this shift");
        }

        // Create and save new registration
        ShiftRegistration shiftRegistration = new ShiftRegistration(guard, shift);
        ShiftRegistration savedShiftRegistration = shiftRegRepo.save(shiftRegistration);

        // Add to guard's registrations
        guard.addShiftRegistration(savedShiftRegistration);

        return savedShiftRegistration;
    }

    public ShiftRegistration updateRegistration(ShiftRegistration shiftRegistration) {
        return shiftRegRepo.save(shiftRegistration);
    }

    public void deleteRegistration(Long registrationId) {
        shiftRegRepo.deleteById(registrationId);
    }

    public List<ShiftRegistration> getPendingRegistrationsByShift(Shift shift) {
        return shiftRegRepo.findByShiftAndRegistrationStatus(shift, RegistrationStatus.PENDING);
    }

    // Admin can approve a pending registration made by a guard.
    public ShiftRegistration approveRegistration(Long registrationId, Admin admin) {
        ShiftRegistration registration = getByRegistrationId(registrationId);

        if (registration.getRegistrationStatus() != RegistrationStatus.PENDING) {
            throw new IllegalArgumentException("Only pending registrations can be approved");
        }

        registration.setRegistrationStatus(RegistrationStatus.APPROVED);
        return shiftRegRepo.save(registration);
    }


    // Admin can reject a pending registration made by a guard.
    public ShiftRegistration rejectRegistration(Long registrationId, Admin admin) {
        ShiftRegistration registration = getByRegistrationId(registrationId);

        if (registration.getRegistrationStatus() != RegistrationStatus.PENDING) {
            throw new IllegalArgumentException("Only pending registrations can be rejected");
        }

        registration.setRegistrationStatus(RegistrationStatus.REJECTED);
        return shiftRegRepo.save(registration);
    }

    // Gives an Admin the ability to forcefully remove a guard from a shift within the system, if it has been mutually agreed.

    public ShiftRegistration removeGuardFromShift(Long registrationId, Admin admin) {
        ShiftRegistration registration = getByRegistrationId(registrationId);
        registration.setRegistrationStatus(RegistrationStatus.CANCELLED);

        //TODO: be able to update shift status so when the amount of guards are < than the required for the shift, its status is set to available.
        return shiftRegRepo.save(registration);
    }

    // A Guard has the ability to remove himself from a shift (Only if the registration is pending).
    public ShiftRegistration cancelRegistration(Long registrationId, Guard guard) {
        ShiftRegistration registration = getByRegistrationId(registrationId);

        if (!registration.getGuard().equals(guard)) {
            throw new IllegalArgumentException("Guards can only cancel their own registrations");
        }

        if (registration.getRegistrationStatus() != RegistrationStatus.PENDING) {
            throw new IllegalArgumentException("Only pending registrations can be cancelled");
        }

        registration.setRegistrationStatus(RegistrationStatus.CANCELLED);
        return shiftRegRepo.save(registration);
    }
}
