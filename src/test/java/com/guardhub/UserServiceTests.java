package com.guardhub;

import com.guardhub.user.Admin;
import com.guardhub.user.Guard;
import com.guardhub.user.User;
import com.guardhub.user.UserRepository;
import com.guardhub.user.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void findAllGuardsReturnsGuardsFromRepository() {
        List<Guard> guards = List.of(
                new Guard(1L, "Guard Guardsen", "password123", "guard1@test.dk", "12345678"),
                new Guard(2L, "Guard Guardsen", "password123", "guard2@test.dk", "87654321")
        );

        when(userRepository.findAllGuards()).thenReturn(guards);

        List<Guard> result = userService.findAllGuards();

        assertEquals(2, result.size());
        assertEquals("Guard Guardsen", result.getFirst().getName());

        verify(userRepository, times(1)).findAllGuards();
    }

    @Test
    void findAllAdminsReturnsAdminsFromRepository() {
        List<Admin> admins = List.of(
                new Admin(1L, "Admin Adminsen", "password123", "admin@test.dk", "12345678")
        );

        when(userRepository.findAllAdmins()).thenReturn(admins);

        List<Admin> result = userService.findAllAdmins();

        assertEquals(1, result.size());
        assertEquals("Admin Adminsen", result.getFirst().getName());

        verify(userRepository, times(1)).findAllAdmins();
    }

    @Test
    void addGuardSavesGuardIfEmailDoesNotExist() {
        Guard guard = new Guard("Guard Guardsen", "password123", "guard@test.dk", "12345678");

        when(userRepository.findUserByEmail("guard@test.dk")).thenReturn(Optional.empty());
        when(userRepository.save(guard)).thenReturn(guard);

        Guard result = userService.addGuard(guard);

        assertEquals("Guard Guardsen", result.getName());
        verify(userRepository, times(1)).findUserByEmail("guard@test.dk");
        verify(userRepository, times(1)).save(guard);
    }

    @Test
    void addGuardThrowsIfEmailAlreadyExists() {
        Guard existingGuard = new Guard(1L, "Guard Guardsen", "password123", "guard@test.dk", "12345678");
        Guard newGuard = new Guard("Guard Guardsen", "password123", "guard@test.dk", "87654321");

        when(userRepository.findUserByEmail("guard@test.dk")).thenReturn(Optional.of(existingGuard));

        assertThrows(IllegalArgumentException.class, () -> userService.addGuard(newGuard));

        verify(userRepository, times(1)).findUserByEmail("guard@test.dk");
        verify(userRepository, never()).save(any());
    }

    @Test
    void loginReturnsUserWhenEmailAndPasswordAreCorrect() {
        Guard guard = new Guard(1L, "Guard Guardsen", "password123", "guard@test.dk", "12345678");

        when(userRepository.findUserByEmail("guard@test.dk")).thenReturn(Optional.of(guard));

        User result = userService.login("guard@test.dk", "password123");

        assertEquals("Guard Guardsen", result.getName());
        verify(userRepository, times(1)).findUserByEmail("guard@test.dk");
    }

    @Test
    void loginThrowsWhenPasswordIsWrong() {
        Guard guard = new Guard(1L, "Guard Guardsen", "password123", "guard@test.dk", "12345678");

        when(userRepository.findUserByEmail("guard@test.dk")).thenReturn(Optional.of(guard));

        assertThrows(IllegalArgumentException.class,
                () -> userService.login("guard@test.dk", "wrongPassword"));

        verify(userRepository, times(1)).findUserByEmail("guard@test.dk");
    }

    @Test
    void loginThrowsWhenEmailDoesNotExist() {
        when(userRepository.findUserByEmail("missing@test.dk")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> userService.login("missing@test.dk", "password123"));

        verify(userRepository, times(1)).findUserByEmail("missing@test.dk");
    }

    @Test
    void deleteUserDeletesIfUserExists() {
        Long userId = 1L;

        when(userRepository.existsById(userId)).thenReturn(true);

        userService.deleteUser(userId);

        verify(userRepository, times(1)).existsById(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void deleteUserThrowsIfUserDoesNotExist() {
        Long userId = 99L;

        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class,
                () -> userService.deleteUser(userId));

        verify(userRepository, times(1)).existsById(userId);
        verify(userRepository, never()).deleteById(any());
    }
}