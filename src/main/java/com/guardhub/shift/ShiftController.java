package com.guardhub.shift;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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

    @PostMapping
    public ResponseEntity<ShiftDTO> createShift(@RequestBody CreateShiftRequest request) {
        ShiftDTO created = shiftService.createShift(request);
        return ResponseEntity.created(URI.create("api/shifts/" + created.id())).body(created);
    }

    @DeleteMapping("/{shiftId}")
    public ResponseEntity<Void> deleteShift(@PathVariable Long shiftId) {
        shiftService.deleteShift(shiftId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{shiftId}")
    public ResponseEntity<ShiftDTO> updateShift(@PathVariable Long shiftId, @RequestBody UpdateShiftRequest request) {
        return ResponseEntity.ok(shiftService.updateShift(shiftId, request));
    }
}
