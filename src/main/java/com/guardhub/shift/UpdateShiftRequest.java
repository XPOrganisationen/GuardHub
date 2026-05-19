package com.guardhub.shift;

import java.time.LocalDateTime;

public record UpdateShiftRequest(
        String title,
        LocalDateTime shiftStart,
        LocalDateTime shiftEnd,
        String description,
        boolean foodServed,
        String parkingInstructions,
        int requiredGuardAmount
) {}
