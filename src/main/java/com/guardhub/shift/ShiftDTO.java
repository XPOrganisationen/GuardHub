package com.guardhub.shift;

import java.time.LocalDateTime;

public record ShiftDTO (
        Long id,
        String title,
        String clientName,
        LocalDateTime shiftStart,
        LocalDateTime shiftEnd,
        String description,
        String location,
        boolean foodServed,
        String clientPhoneNumber,
        String parkingInstructions,
        int requiredGuards,
        Long registrations
) { }
