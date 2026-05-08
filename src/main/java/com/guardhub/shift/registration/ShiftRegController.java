package com.guardhub.shift.registration;

import com.guardhub.shift.Shift;
import com.guardhub.user.User;
import com.guardhub.user.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController

@RequestMapping("api/shiftRegistration")
public class ShiftRegController {

    private final ShiftRegService shiftRegService;
    private final UserRepository userRepository;

    public ShiftRegController (ShiftRegService shiftRegService, UserRepository userRepository) {
        this.shiftRegService = shiftRegService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<ShiftRegistration> getAllRegistrations() {
        return shiftRegService.findAllRegistrations();
    }

    @GetMapping("/{registrationId}")
    public Optional<ShiftRegistration> getAllRegistrationsById(@PathVariable Long registrationId) {
        return shiftRegService.findByRegId(registrationId);
    }

    @GetMapping("/pending")
    public List<ShiftRegistration> getPendingRegistrationsByShift(@RequestBody Shift shift) {
        return shiftRegService.getPendingRegistrationsByShift(shift);
    }

    @PostMapping
    public ShiftRegistration addRegistration(@RequestBody ShiftRegistration shiftRegistration) {
        return shiftRegService.addRegistration(shiftRegistration);
    }

    @PostMapping("/{registrationId}/approve")
    public ShiftRegistration approveRegistration(@PathVariable Long registrationId, @RequestParam Long adminId) {
       User admin = userRepository.findById(adminId).orElseThrow();
        return shiftRegService.approveRegistration(registrationId, admin);
    }

    @PostMapping("/{registrationId}/reject")
    public ShiftRegistration rejectRegistration(@PathVariable Long registrationId, @RequestParam Long adminId) {
        User admin = userRepository.findById(adminId).orElseThrow();
        return shiftRegService.rejectRegistration(registrationId, admin);
    }

    @PostMapping("/{registrationId}/cancel")
    public ShiftRegistration cancelRegistration(@PathVariable Long registrationId, @RequestParam Long guardId) {
        User guard = userRepository.findById(guardId).orElseThrow();
        return shiftRegService.cancelRegistration(registrationId, guard);
    }

    @PutMapping
    public ShiftRegistration updateRegistration(@RequestBody ShiftRegistration shiftRegistration) {
        return shiftRegService.updateRegistration(shiftRegistration);
    }

    @DeleteMapping("/{registrationId}/remove")
    public ShiftRegistration removeGuardFromShift(@PathVariable Long registrationId, @RequestParam Long adminId) {
        User admin = userRepository.findById(adminId).orElseThrow();
        return shiftRegService.removeGuardFromShift(registrationId, admin);
    }

    @DeleteMapping("/{registrationId}")
    public void deleteRegistration(@PathVariable Long registrationId) {
        shiftRegService.deleteRegistration(registrationId);
    }
}
