package com.guardhub.shift.registration;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController

@RequestMapping("api/shiftRegistration")
public class ShiftRegController {

    private final ShiftRegService shiftRegService;

    public ShiftRegController (ShiftRegService shiftRegService) {
        this.shiftRegService = shiftRegService;
    }

    @GetMapping
    public List<ShiftRegistration> getAllRegistrations() {
        return shiftRegService.findAllRegistrations();
    }

    @GetMapping("{registrationId}")
    public Optional<ShiftRegistration> getAllRegById(@PathVariable Long registrationId) {
        return shiftRegService.findByRegId(registrationId);
    }

    @PostMapping
    public ShiftRegistration addRegistration(@RequestBody ShiftRegistration shiftRegistration) {
        return shiftRegService.addRegistration(shiftRegistration);
    }

    @PutMapping
    public ShiftRegistration updateRegistration(@RequestBody ShiftRegistration shiftRegistration) {
        return shiftRegService.updateRegistration(shiftRegistration);
    }

    @DeleteMapping("{registrationId}")
    public void deleteRegistration(@PathVariable Long registrationId) {
        shiftRegService.deleteRegistration(registrationId);
    }
}
