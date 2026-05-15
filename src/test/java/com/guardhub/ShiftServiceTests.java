package com.guardhub;

import com.guardhub.exceptions.EntityDoesNotExistException;
import com.guardhub.shift.ShiftDTO;
import com.guardhub.shift.ShiftRepository;
import com.guardhub.shift.ShiftServiceImpl;
import com.guardhub.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShiftServiceTests {

    @Mock
    private ShiftRepository shiftRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ShiftServiceImpl shiftService;

    private ShiftDTO shift(
            Long id,
            LocalDateTime start,
            LocalDateTime end,
            int required,
            long registrations
    ) {
        return new ShiftDTO(
                id,
                "Title " + id,
                "exampleClient",
                start,
                end,
                "Description " + id,
                "HQ",
                false,
                "12345678",
                "No parking!",
                required,
                registrations
        );
    }

    @Test
    void findAllForWeekReturnsSortedAndGroupedResults() {
        Long weekOffset = 0L;

        LocalDateTime monday = LocalDate.now()
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                .atStartOfDay();

        List<ShiftDTO> shifts = new ArrayList<>(List.of(
                shift(1L, monday.plusHours(10), monday.plusHours(18), 5, 2), // 3 spots
                shift(2L, monday.plusHours(8), monday.plusHours(16), 4, 1), // 3 spots, earliest
                shift(3L, monday.plusHours(12), monday.plusHours(20), 6, 6)  // 0 spots, latest
        ));

        /* Why any()? Because we don't care about the two dates being exactly equal
           We want to demonstrate sorting, grouping behavior. Stubbing errors arise if we don't
            because the current time is changing all the time and our test date is thus not equal.
         */
        when(shiftRepository.findAllForWeek(any(), any())).thenReturn(shifts);

        Map<DayOfWeek, List<ShiftDTO>> result = shiftService.findAllForWeek(weekOffset);

        assertEquals(1, result.size());
        List<ShiftDTO> mondayShifts = result.get(DayOfWeek.MONDAY);
        assertNotNull(mondayShifts);
        assertEquals(3, mondayShifts.size());

        assertEquals(2L, mondayShifts.get(0).id()); // 3 spots, earliest
        assertEquals(1L, mondayShifts.get(1).id()); // 3 spots, later
        assertEquals(3L, mondayShifts.get(2).id()); // 0 spots, latest

        verify(shiftRepository).findAllForWeek(any(), any());
    }


    @Test
    void findAllForGuardForWeekThrowsIfGuardDoesNotExist() {
        Long guardId = 99L;
        when(userRepository.existsById(guardId)).thenReturn(false);

        assertThrows(EntityDoesNotExistException.class,
                () -> shiftService.findAllForGuardForWeek(guardId, 0L));

        verify(userRepository).existsById(guardId);
        verifyNoInteractions(shiftRepository);
    }

    @Test
    void findAllForGuardForWeekReturnsSortedAndGroupedResults() {
        Long guardId = 10L;
        Long weekOffset = 1L;

        when(userRepository.existsById(guardId)).thenReturn(true);

        LocalDateTime monday = LocalDate.now()
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                .plusWeeks(1)
                .atStartOfDay();

        List<ShiftDTO> shifts = new ArrayList<>(List.of(
                shift(1L, monday.plusHours(9), monday.plusHours(17), 3, 1),
                shift(2L, monday.plusHours(7), monday.plusHours(15), 4, 4)
        ));

        when(shiftRepository.findAllForGuardForWeek(eq(guardId), any(), any()))
                .thenReturn(shifts);

        Map<DayOfWeek, List<ShiftDTO>> result =
                shiftService.findAllForGuardForWeek(guardId, weekOffset);

        assertEquals(1, result.size());
        List<ShiftDTO> mondayShifts = result.get(DayOfWeek.MONDAY);
        assertNotNull(mondayShifts);
        assertEquals(2, mondayShifts.size());

        verify(userRepository).existsById(guardId);
        verify(shiftRepository).findAllForGuardForWeek(eq(guardId), any(), any());
    }
}