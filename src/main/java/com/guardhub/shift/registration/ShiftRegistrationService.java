package com.guardhub.shift.registration;

import com.guardhub.exceptions.EntityDoesNotExistException;
import com.guardhub.shift.Shift;
import com.guardhub.shift.ShiftRepository;
import com.guardhub.user.User;
import com.guardhub.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShiftRegistrationService {

    private final ShiftRegistrationRepository shiftRegistrationRepository;
    private final UserRepository userRepository;
    private final ShiftRepository shiftRepository;

    public ShiftRegistrationService(ShiftRegistrationRepository shiftRegistrationRepository, UserRepository userRepository, ShiftRepository shiftRepository) {
        this.shiftRegistrationRepository = shiftRegistrationRepository;
        this.userRepository = userRepository;
        this.shiftRepository = shiftRepository;
    }

    public List<ShiftRegistration> findAllRegistrations() {
        return shiftRegistrationRepository.findAll();
    }

    public ShiftRegistration findByRegId(Long registrationId) {
        return shiftRegistrationRepository.findById(registrationId).orElseThrow(() -> new EntityDoesNotExistException("No registration with ID " + registrationId + " exists"));
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
            // Making sure you cannot sign up for shift that's already taking place or has taken place.
        if (shift.getShiftStart().isBefore(java.time.LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot register for a shift in the past");
        }
            // Double booking prevention:
        if (shiftRegistrationRepository.findByGuardAndShift(guard, shift).isPresent()) {
            throw new IllegalArgumentException("Guard is already registered for this shift");
        }
            // By default, set shift registration to pending, until an admin has approved the selection(s) of guards.
        shiftRegistration.setRegistrationStatus(RegistrationStatus.PENDING);
        return shiftRegistrationRepository.save(shiftRegistration);
    }

    public ShiftRegistration updateRegistration(ShiftRegistration shiftRegistration) {
        return shiftRegistrationRepository.save(shiftRegistration);
    }

    public void deleteRegistration(Long registrationId) {
        shiftRegistrationRepository.deleteById(registrationId);
    }

    public List<ShiftRegistration> getPendingRegistrationsByShift(Shift shift) {
        return shiftRegistrationRepository.findByShiftAndRegistrationStatus(shift, RegistrationStatus.PENDING);
    }

        // Admin can approve a pending registration made by a guard.
    public ShiftRegistration approveRegistration(Long registrationId, User admin) {
        if (!admin.isAdmin()) {
            throw new IllegalArgumentException("Only admins can approve shift registrations");
        }

        ShiftRegistration registration = shiftRegistrationRepository.findById(registrationId)
                .orElseThrow(() -> new IllegalArgumentException("No registration found with id: " + registrationId));

        registration.setRegistrationStatus(RegistrationStatus.APPROVED);
        return shiftRegistrationRepository.save(registration);
    }

        // Admin can reject a pending registration made by a guard.
    public ShiftRegistration rejectRegistration(Long registrationId, User admin) {
        if (!admin.isAdmin()) {
            throw new IllegalArgumentException("Only admins can reject shift registrations");
        }

        ShiftRegistration registration = shiftRegistrationRepository.findById(registrationId)
                .orElseThrow(() -> new IllegalArgumentException("No registration found with id: " + registrationId));

        registration.setRegistrationStatus(RegistrationStatus.REJECTED);
        return shiftRegistrationRepository.save(registration);
    }

        // Gives an Admin the ability to forcefully remove a guard from a shift within the system, if it has been mutually agreed.
    public ShiftRegistration removeGuardFromShift(Long registrationId, User admin) {
        if (!admin.isAdmin()) {
            throw new IllegalArgumentException("Only admins can remove guards from shifts");
        }

        ShiftRegistration registration = shiftRegistrationRepository.findById(registrationId)
                .orElseThrow(() -> new IllegalArgumentException("No registration found with id: " + registrationId));

        registration.setRegistrationStatus(RegistrationStatus.CANCELED);
        shiftRegistrationRepository.save(registration);

        //TODO: be able to update shift status so when the amount of guards are < than the required for the shift, its status is set to available.
        return shiftRegistrationRepository.save(registration);
    }

    // A Guard has the ability to remove himself from a shift (Only if the registration is pending).
    public ShiftRegistration cancelRegistration(Long registrationId, User guard) {
        ShiftRegistration registration = shiftRegistrationRepository.findById(registrationId)
                .orElseThrow(() -> new IllegalArgumentException("No registration found with id: " + registrationId));

        if (!registration.getGuard().equals(guard)) {
            throw new IllegalArgumentException("Guards can only cancel their own registrations");
        }

        if (registration.getRegistrationStatus() != RegistrationStatus.PENDING) {
            throw new IllegalArgumentException("Only pending registrations can be cancelled");
        }

        registration.setRegistrationStatus(RegistrationStatus.CANCELED);
        return shiftRegistrationRepository.save(registration);
    }

    public List<String> findAcceptedGuardNamesByShiftId(Long shiftId) {
        return shiftRegistrationRepository.findAcceptedGuardNamesByShiftId(shiftId);
    }

    public Boolean guardHasRegistrationForShift(Long guardId, Long shiftId) {
        return shiftRegistrationRepository.guardHasRegistrationForShift(guardId, shiftId);
    }

    public void deleteRegistrationByShiftIdAndGuardId(Long shiftId, Long guardId) {
        shiftRegistrationRepository.deleteByShiftIdAndGuardId(shiftId, guardId);
    }

    public ShiftRegistration addRegistrationByGuardAndShiftIds(Long guardId, Long shiftId) {
        User guard = userRepository.findById(guardId).orElseThrow(() -> new EntityDoesNotExistException("No guard with ID " + guardId + " exists"));
        Shift shift = shiftRepository.findById(shiftId).orElseThrow(() -> new EntityDoesNotExistException("No shift with ID " + shiftId + " exists"));
        return addRegistration(new ShiftRegistration(null, guard, shift, RegistrationStatus.PENDING));
    }
}
