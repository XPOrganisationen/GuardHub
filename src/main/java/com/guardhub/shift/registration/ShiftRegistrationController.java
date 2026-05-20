package com.guardhub.shift.registration;

import com.guardhub.shift.Shift;
import com.guardhub.user.Admin;
import com.guardhub.user.Guard;
import com.guardhub.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;
import java.util.List;

@RestController

@RequestMapping("api/registrations")
public class ShiftRegistrationController {
    private final ShiftRegistrationService shiftRegistrationService;
    private final UserService userService;


    public ShiftRegistrationController(ShiftRegistrationService shiftRegistrationService, UserService userService) {
        this.shiftRegistrationService = shiftRegistrationService;
        this.userService = userService;
    }

    @GetMapping
    public List<ShiftRegistration> getAllRegistrations() {
        return shiftRegistrationService.findAllRegistrations();
    }

    @GetMapping("/registration-status/{status}")
    public List<ShiftRegistrationDTO> getRegistrationsByStatus(@PathVariable RegistrationStatus status) {
        return shiftRegistrationService.findByRegistrationStatus(status).stream()
                .map(ShiftRegistrationDTO::from)
                .toList();
    }

    @GetMapping("/{registrationId}")
    public ResponseEntity<ShiftRegistration> getAllRegistrationsById(@PathVariable Long registrationId) {
        return ResponseEntity.ok(shiftRegistrationService.findByRegId(registrationId));
    }

    @GetMapping("/guards/{shiftId}")
    public List<String> getAllRegisteredGuardNamesForShift(@PathVariable Long shiftId) {
        return shiftRegistrationService.findAcceptedGuardNamesByShiftId(shiftId);
    }

    @GetMapping("/{shiftId}/has-registration/{guardId}")
    public ResponseEntity<Boolean> guardHasRegistrationForShift(@PathVariable Long guardId, @PathVariable Long shiftId) {
        return ResponseEntity.ok(shiftRegistrationService.guardHasRegistrationForShift(guardId, shiftId));
    }

    @GetMapping("/pending")
    public List<ShiftRegistration> getPendingRegistrationsByShift(@RequestBody Shift shift) {
        return shiftRegistrationService.getPendingRegistrationsByShift(shift);
    }

    @PostMapping
    public ResponseEntity<ShiftRegistration> addRegistration(@RequestBody ShiftRegistration shiftRegistration) {
        return ResponseEntity.ok(shiftRegistrationService.addRegistration(shiftRegistration));
    }

    @PostMapping("/by-guard-id-and-shift-id/{guardId}-{shiftId}")
    public ResponseEntity<ShiftRegistration> addRegistrationByGuardIdAndShiftId(@PathVariable Long guardId, @PathVariable Long shiftId) {
        return ResponseEntity.ok(shiftRegistrationService.addRegistrationByGuardAndShiftIds(guardId, shiftId));
    }

    @PostMapping("/{registrationId}/approve")
    public ResponseEntity<ShiftRegistration> approveRegistration(@PathVariable Long registrationId, @RequestParam Long adminId) {
        Admin admin = (Admin) userService.findById(adminId);
        return ResponseEntity.ok(shiftRegistrationService.approveRegistration(registrationId, admin));
    }

    @PostMapping("/{registrationId}/reject")
    public ResponseEntity<ShiftRegistration> rejectRegistration(@PathVariable Long registrationId, @RequestParam Long adminId) {
        Admin admin = (Admin) userService.findById(adminId);
        return ResponseEntity.ok(shiftRegistrationService.rejectRegistration(registrationId, admin));
    }

    @PostMapping("/{registrationId}/cancel")
    public ResponseEntity<ShiftRegistration> cancelRegistration(@PathVariable Long registrationId, @RequestParam Long guardId) {
        Guard guard = (Guard) userService.findById(guardId);
        return ResponseEntity.ok(shiftRegistrationService.cancelRegistration(registrationId, guard));
    }

    @PutMapping
    public ResponseEntity<ShiftRegistration> updateRegistration(@RequestBody ShiftRegistration shiftRegistration) {
        ShiftRegistration updated = shiftRegistrationService.updateRegistration(shiftRegistration);
        return ResponseEntity.created(URI.create("api/registrations/" + updated.getRegistrationId())).body(updated);
    }

    @DeleteMapping("/{registrationId}/remove")
    public ResponseEntity<Void> removeGuardFromShift(@PathVariable Long registrationId, @RequestParam Long adminId) {
        Admin admin = (Admin) userService.findById(adminId);
        shiftRegistrationService.removeGuardFromShift(registrationId, admin);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{registrationId}")
    public ResponseEntity<Void> deleteRegistration(@PathVariable Long registrationId) {
        shiftRegistrationService.deleteRegistration(registrationId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete-by-shift-and-guard/{shiftId}-{guardId}")
    public ResponseEntity<Void> deleteRegistrationForShift(@PathVariable Long shiftId, @PathVariable Long guardId) {
        shiftRegistrationService.deleteRegistrationByShiftIdAndGuardId(shiftId, guardId);
        return ResponseEntity.noContent().build();
    }
}
