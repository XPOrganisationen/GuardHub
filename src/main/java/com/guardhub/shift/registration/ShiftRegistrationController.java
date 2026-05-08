package com.guardhub.shift.registration;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/shiftRegistration")
public class ShiftRegistrationController {

    private final ShiftRegistrationService shiftRegistrationService;

    public ShiftRegistrationController(ShiftRegistrationService shiftRegistrationService) {
        this.shiftRegistrationService = shiftRegistrationService;
    }

    @GetMapping
    public List<ShiftRegistration> getAllRegistrations() {
        return shiftRegistrationService.findAllRegistrations();
    }

    @GetMapping("/{registrationId}")
    public ResponseEntity<ShiftRegistration> getRegistrationById(@PathVariable Long registrationId) {
        return shiftRegistrationService.findByRegistrationId(registrationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/pending")
    public List<ShiftRegistration> getPendingRegistrations() {
        return shiftRegistrationService.findByRegistrationStatus(RegistrationStatus.PENDING);
    }

    // Udkommenteret da jeg regner med at Filip laver endpoints :) - Thor
//    @PostMapping
//    public ShiftRegistration addRegistration(@RequestBody ShiftRegistration shiftRegistration) {
//        return shiftRegistrationService.addRegistration(shiftRegistration);
//    }

    @PutMapping
    public ShiftRegistration updateRegistration(@RequestBody ShiftRegistration shiftRegistration) {
        return shiftRegistrationService.updateRegistration(shiftRegistration);
    }

    @DeleteMapping("{registrationId}")
    public void deleteRegistration(@PathVariable Long registrationId) {
        shiftRegistrationService.deleteRegistration(registrationId);
    }
}
