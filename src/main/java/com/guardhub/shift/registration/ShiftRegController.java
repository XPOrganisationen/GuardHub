package com.guardhub.shift.registration;

import com.guardhub.shift.Shift;
import com.guardhub.user.Admin;
import com.guardhub.user.Guard;
import com.guardhub.user.User;
import com.guardhub.user.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/shiftRegistration")
public class ShiftRegController {

    private final ShiftRegService shiftRegService;
    private final UserService userService;

    public ShiftRegController(ShiftRegService shiftRegService, UserService userService) {
        this.shiftRegService = shiftRegService;
        this.userService = userService;
    }

    @GetMapping
    public List<ShiftRegistration> getAllRegistrations() {
        return shiftRegService.findAllRegistrations();
    }

    @GetMapping("/{registrationId}")
    public Optional<ShiftRegistration> getRegistrationById(@PathVariable Long registrationId) {
        return shiftRegService.findByRegistrationId(registrationId);
    }


    @GetMapping("/pending/all")
    public List<ShiftRegistration> getPendingRegistrations(){
        return shiftRegService.findByRegistrationStatus(RegistrationStatus.PENDING);
    }

    @GetMapping("/pending/by-shift")
    public List<ShiftRegistration> getPendingRegistrationsByShift(@RequestBody Shift shift) {
        return shiftRegService.getPendingRegistrationsByShift(shift);
    }

    @PostMapping
    public ShiftRegistration addRegistration(@RequestBody ShiftRegistration shiftRegistration) {
        return shiftRegService.registerGuardForShift(
                shiftRegistration.getGuard(),
                shiftRegistration.getShift()
        );
    }

    @PostMapping("/{registrationId}/approve")
    public ShiftRegistration approveRegistration(@PathVariable Long registrationId, @RequestParam Long adminId) {
        User user = userService.findById(adminId);
        Admin admin = (Admin) user;

        return shiftRegService.approveRegistration(registrationId, admin);
    }

    @PostMapping("/{registrationId}/reject")
    public ShiftRegistration rejectRegistration(@PathVariable Long registrationId, @RequestParam Long adminId) {
        User user = userService.findById(adminId);
        Admin admin = (Admin) user;

        return shiftRegService.rejectRegistration(registrationId, admin);
    }

    @PostMapping("/{registrationId}/cancel")
    public ShiftRegistration cancelRegistration(@PathVariable Long registrationId, @RequestParam Long guardId) {
        User user = userService.findById(guardId);
        Guard guard = (Guard) user;

        return shiftRegService.cancelRegistration(registrationId, guard);
    }

    @PutMapping
    public ShiftRegistration updateRegistration(@RequestBody ShiftRegistration shiftRegistration) {
        return shiftRegService.updateRegistration(shiftRegistration);
    }

    @DeleteMapping("/{registrationId}/remove")
    public ShiftRegistration removeGuardFromShift(@PathVariable Long registrationId, @RequestParam Long adminId) {
        User user = userService.findById(adminId);
        Admin admin = (Admin) user;

        return shiftRegService.removeGuardFromShift(registrationId, admin);
    }

    @DeleteMapping("/{registrationId}")
    public void deleteRegistration(@PathVariable Long registrationId) {
        shiftRegService.deleteRegistration(registrationId);
    }
}