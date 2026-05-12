package com.guardhub.shift;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

public interface ShiftService {
    Map<DayOfWeek, List<ShiftDTO>> findAllForWeek(Long weekOffset);
    Map<DayOfWeek, List<ShiftDTO>> findAllForGuardForWeek(Long guardId, Long weekOffset);
}
