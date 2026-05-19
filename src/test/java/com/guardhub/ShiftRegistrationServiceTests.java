package com.guardhub;

import com.guardhub.exceptions.EntityDoesNotExistException;
import com.guardhub.shift.Shift;
import com.guardhub.shift.ShiftRepository;
import com.guardhub.shift.registration.RegistrationStatus;
import com.guardhub.shift.registration.ShiftRegistration;
import com.guardhub.shift.registration.ShiftRegistrationRepository;
import com.guardhub.shift.registration.ShiftRegistrationService;
import com.guardhub.user.Guard;
import com.guardhub.user.User;
import com.guardhub.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShiftRegistrationServiceTests {
    @Mock
    private ShiftRegistrationRepository shiftRegistrationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ShiftRepository shiftRepository;

    @InjectMocks
    private ShiftRegistrationService service;

    @Test
    void findAcceptedGuardsByShiftIdReturnsExpectedGuards() {
        Long shiftId = 123L;
        List<Guard> expectedGuards = List.of(new Guard("Alice", "1", "1", "1"), new Guard("Bob", "2", "2", "2"));
        when(shiftRepository.existsById(shiftId)).thenReturn(true);
        when(shiftRegistrationRepository.findAcceptedGuardsByShiftId(shiftId)).thenReturn(expectedGuards);

        List<Guard> actual = service.findAcceptedGuardsByShiftId(shiftId);

        assertEquals(expectedGuards, actual);

        verify(shiftRegistrationRepository, times(1)).findAcceptedGuardsByShiftId(shiftId);
        verify(shiftRepository, times(1)).existsById(shiftId);
        verifyNoMoreInteractions(shiftRegistrationRepository);
    }

    @Test
    void findAcceptedGuardsByShiftIdThrowsOnNonExistentShiftId() {
        Long shiftId = 123L;

        when(shiftRepository.existsById(shiftId)).thenReturn(false);

        EntityDoesNotExistException ex = assertThrows(EntityDoesNotExistException.class,
                () -> service.findAcceptedGuardsByShiftId(shiftId));
        assertTrue(ex.getMessage().contains("No shift with ID " + shiftId));

        verify(shiftRepository, times(1)).existsById(shiftId);
        verifyNoMoreInteractions(shiftRepository);
    }

    @Test
    void guardHasRegistrationForShiftReturnsTrueIfGuardHasRegistration() {
        Long guardId = 10L;
        Long shiftId = 20L;
        when(shiftRepository.existsById(shiftId)).thenReturn(true);
        when(userRepository.existsById(guardId)).thenReturn(true);
        when(shiftRegistrationRepository.guardHasRegistrationForShift(guardId, shiftId)).thenReturn(Boolean.TRUE);

        Boolean result = service.guardHasRegistrationForShift(guardId, shiftId);

        assertTrue(result);
        verify(shiftRegistrationRepository, times(1)).guardHasRegistrationForShift(guardId, shiftId);
        verify(shiftRepository, times(1)).existsById(shiftId);
        verify(userRepository, times(1)).existsById(guardId);
        verifyNoMoreInteractions(shiftRegistrationRepository);
    }

    @Test
    void guardHasRegistrationForShiftReturnsFalseIfShiftAndGuardExistButGuardNotRegisteredForShift() {
        Long guardId = 11L;
        Long shiftId = 21L;
        when(shiftRepository.existsById(shiftId)).thenReturn(true);
        when(userRepository.existsById(guardId)).thenReturn(true);
        when(shiftRegistrationRepository.guardHasRegistrationForShift(guardId, shiftId)).thenReturn(Boolean.FALSE);

        Boolean result = service.guardHasRegistrationForShift(guardId, shiftId);

        assertFalse(result);
        verify(shiftRegistrationRepository, times(1)).guardHasRegistrationForShift(guardId, shiftId);
        verify(shiftRepository, times(1)).existsById(shiftId);
        verify(userRepository, times(1)).existsById(guardId);
        verifyNoMoreInteractions(shiftRegistrationRepository);
    }

    @Test
    void guardHasRegistrationForShiftThrowsIfShiftDoesNotExist() {
        Long guardId = 11L;
        Long shiftId = 21L;
        when(shiftRepository.existsById(shiftId)).thenReturn(false);

        EntityDoesNotExistException ex = assertThrows(EntityDoesNotExistException.class,
                () -> service.guardHasRegistrationForShift(guardId, shiftId));
        assertTrue(ex.getMessage().contains("No shift with ID " + shiftId));

        verify(shiftRepository, times(1)).existsById(shiftId);
        verify(userRepository, times(0)).existsById(guardId);
        verifyNoInteractions(shiftRegistrationRepository);
    }

    @Test
    void guardHasRegistrationForShiftThrowsIfGuardDoesNotExist() {
        Long guardId = 11L;
        Long shiftId = 21L;
        when(shiftRepository.existsById(shiftId)).thenReturn(true);
        when(userRepository.existsById(guardId)).thenReturn(false);

        EntityDoesNotExistException ex = assertThrows(EntityDoesNotExistException.class,
                () -> service.guardHasRegistrationForShift(guardId, shiftId));
        assertTrue(ex.getMessage().contains("No guard with ID " + guardId));

        verify(userRepository, times(1)).existsById(guardId);
        verifyNoInteractions(shiftRegistrationRepository);
    }

    @Test
    void deleteRegistrationByShiftIdAndGuardIdCallsRepositoryIfShiftAndGuardExist() {
        Long shiftId = 5L;
        Long guardId = 6L;

        when(shiftRepository.existsById(shiftId)).thenReturn(true);
        when(userRepository.existsById(guardId)).thenReturn(true);
        service.deleteRegistrationByShiftIdAndGuardId(shiftId, guardId);

        verify(shiftRegistrationRepository, times(1)).deleteByShiftIdAndGuardId(shiftId, guardId);
        verify(shiftRepository, times(1)).existsById(shiftId);
        verify(userRepository, times(1)).existsById(guardId);
        verifyNoMoreInteractions(shiftRegistrationRepository);
    }

    @Test
    void deleteRegistrationByShiftIdAndGuardIdThrowsOnNonExistentShiftId() {
        Long shiftId = 5L;
        Long guardId = 6L;

        when(shiftRepository.existsById(shiftId)).thenReturn(false);

        EntityDoesNotExistException ex = assertThrows(EntityDoesNotExistException.class,
                () -> service.deleteRegistrationByShiftIdAndGuardId(shiftId, guardId));
        assertTrue(ex.getMessage().contains("No shift with ID " + shiftId));

        verify(shiftRepository, times(1)).existsById(shiftId);
        verify(shiftRegistrationRepository, times(0)).deleteByShiftIdAndGuardId(shiftId, guardId);
        verifyNoMoreInteractions(shiftRegistrationRepository);
    }

    @Test
    void deleteRegistrationByShiftIdAndGuardIdThrowsOnNonExistentGuardId() {
        Long shiftId = 5L;
        Long guardId = 6L;

        when(shiftRepository.existsById(shiftId)).thenReturn(true);
        when(userRepository.existsById(guardId)).thenReturn(false);

        EntityDoesNotExistException ex = assertThrows(EntityDoesNotExistException.class,
                () -> service.deleteRegistrationByShiftIdAndGuardId(shiftId, guardId));
        assertTrue(ex.getMessage().contains("No guard with ID " + guardId));

        verify(shiftRepository, times(1)).existsById(shiftId);
        verify(userRepository, times(1)).existsById(guardId);
        verify(shiftRegistrationRepository, times(0)).deleteByShiftIdAndGuardId(shiftId, guardId);
        verifyNoMoreInteractions(shiftRegistrationRepository);
    }

    @Test
    void addRegistrationByGuardAndShiftIdsCreatesPendingRegistrationIfShiftAndGuardExist() {
        Long guardId = 2L;
        Long shiftId = 3L;

        Guard guard = new Guard();
        when(userRepository.findById(guardId)).thenReturn(Optional.of(guard));

        Shift shift = new Shift();
        shift.setShiftStart(LocalDateTime.now().plusHours(1));
        shift.setShiftEnd(LocalDateTime.now().plusHours(9));
        when(shiftRepository.findById(shiftId)).thenReturn(Optional.of(shift));

        ShiftRegistration saved = new ShiftRegistration(99L, guard, shift, RegistrationStatus.PENDING);
        when(shiftRegistrationRepository.save(any(ShiftRegistration.class))).thenReturn(saved);

        ShiftRegistration result = service.addRegistrationByGuardAndShiftIds(guardId, shiftId);

        assertNotNull(result);
        assertEquals(saved, result);

        verify(userRepository, times(1)).findById(guardId);
        verify(shiftRepository, times(1)).findById(shiftId);
        verify(shiftRegistrationRepository, times(1)).save(argThat(sr ->
                sr.getGuard() == guard &&
                        sr.getShift() == shift &&
                        sr.getRegistrationStatus() == RegistrationStatus.PENDING
        ));
    }

    @Test
    void addRegistrationByGuardAndShiftIdsGuardThrowsEntityDoesNotExistExceptionWhenGuardDoesNotExist() {
        Long guardId = 100L;
        Long shiftId = 200L;

        when(userRepository.findById(guardId)).thenReturn(Optional.empty());

        EntityDoesNotExistException ex = assertThrows(EntityDoesNotExistException.class,
                () -> service.addRegistrationByGuardAndShiftIds(guardId, shiftId));
        assertTrue(ex.getMessage().contains("No guard with ID " + guardId));

        verify(userRepository, times(1)).findById(guardId);
        verifyNoInteractions(shiftRepository);
        verifyNoInteractions(shiftRegistrationRepository);
    }

    @Test
    void addRegistrationByGuardAndShiftIdsShiftThrowsEntityDoesNotExistExceptionWhenShiftDoesNotExist() {
        Long guardId = 101L;
        Long shiftId = 202L;

        Guard guard = new Guard();
        when(userRepository.findById(guardId)).thenReturn(Optional.of(guard));
        when(shiftRepository.findById(shiftId)).thenReturn(Optional.empty());

        EntityDoesNotExistException ex = assertThrows(EntityDoesNotExistException.class,
                () -> service.addRegistrationByGuardAndShiftIds(guardId, shiftId));
        assertTrue(ex.getMessage().contains("No shift with ID " + shiftId));

        verify(userRepository, times(1)).findById(guardId);
        verify(shiftRepository, times(1)).findById(shiftId);
        verifyNoInteractions(shiftRegistrationRepository);
    }
}
