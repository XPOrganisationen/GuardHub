package com.guardhub.shift;

import java.time.LocalDateTime;

public record ShiftDTO (
        Long id,
        String title,
        LocalDateTime shiftStart,
        LocalDateTime shiftEnd,
        String description,
        String location,
        int requiredGuards,
        Long registrations
) { }
