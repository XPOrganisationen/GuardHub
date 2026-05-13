package com.guardhub.shift;

import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/shifts")
public class ShiftController {
    private final ShiftService shiftService;

    public ShiftController(ShiftService shiftService) {
        this.shiftService = shiftService;
    }

    @GetMapping
    public Map<DayOfWeek, List<ShiftDTO>> fetchShiftsForWeek(@RequestParam Long weekOffset) {
        return shiftService.findAllForWeek(weekOffset);
    }

    @GetMapping("/for-guard/{guardId}")
    public Map<DayOfWeek, List<ShiftDTO>> fetchShiftsForWeekForGuard(@PathVariable Long guardId, @RequestParam Long weekOffset) {
        return shiftService.findAllForGuardForWeek(guardId, weekOffset);
    }
}
