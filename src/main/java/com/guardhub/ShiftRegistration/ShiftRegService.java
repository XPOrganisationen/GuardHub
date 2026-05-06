package com.guardhub.ShiftRegistration;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShiftRegService {

    private final ShiftRegRepo shiftRegRepo;

    public ShiftRegService (ShiftRegRepo shiftRegRepo) {
        this.shiftRegRepo = shiftRegRepo;
    }

    public List<ShiftRegistration> findAllRegistrations() {
        return shiftRegRepo.findAll();
    }

    public Optional<ShiftRegistration> findByRegId(Long registrationId) {
        return shiftRegRepo.findById(registrationId);
    }

    public ShiftRegistration addRegistration(ShiftRegistration shiftRegistration) {
        //venter
        return shiftRegRepo.save(shiftRegistration);
    }

    public ShiftRegistration updateRegistration(ShiftRegistration shiftRegistration) {
        return shiftRegRepo.save(shiftRegistration);
    }

    public void deleteRegistration(Long registrationId) {
        shiftRegRepo.deleteById(registrationId);
    }

    //Alle specifike metoder til dette projekt skrives her efter Guard/User og Shift class er blevet oprettet:)
}
