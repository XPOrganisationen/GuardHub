package com.guardhub.shift;

import java.time.LocalDateTime;

public record CreateShiftRequest(
        String title,
        Long clientId,
        LocalDateTime shiftStart,
        LocalDateTime shiftEnd,
        String description,
        int requiredGuardAmount,
        boolean foodServed,
        String parkingInstructions
) {}
