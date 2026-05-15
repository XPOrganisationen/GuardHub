package com.guardhub;


import com.guardhub.shift.registration.RegistrationStatus;
import com.guardhub.shift.registration.ShiftRegistration;
import com.guardhub.shift.registration.ShiftRegistrationController;
import com.guardhub.shift.registration.ShiftRegistrationService;
import com.guardhub.user.Admin;
import com.guardhub.user.Guard;
import com.guardhub.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ShiftRegistrationController.class)
class ShiftRegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShiftRegistrationService shiftRegService;

    @MockBean
    private UserService userService;

    @Test
    void addRegistration_Success() throws Exception {
        // Arrange
        ShiftRegistration registration = new ShiftRegistration();
        registration.setRegistrationId(1L);
        registration.setRegistrationStatus(RegistrationStatus.PENDING);

        when(shiftRegService.addRegistration(any(ShiftRegistration.class)))
                .thenReturn(registration);

        // Act & Assert
        mockMvc.perform(post("/api/shiftRegistration")
                .contentType("application/json")
                .content("{\"registrationId\":1,\"regStatus\":\"PENDING\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.regStatus").value("PENDING"));
    }

    @Test
    void approveRegistration_Success() throws Exception {
        // Arrange
        Admin admin = new Admin(1L, "Admin User", "password123", "admin@test.com", "12345678");
        ShiftRegistration registration = new ShiftRegistration();
        registration.setRegistrationId(1L);
        registration.setRegistrationStatus(RegistrationStatus.APPROVED);

        when(userService.findById(1L)).thenReturn(admin);
        when(shiftRegService.approveRegistration(1L, admin)).thenReturn(registration);

        // Act & Assert
        mockMvc.perform(post("/api/shiftRegistration/1/approve?adminId=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.regStatus").value("APPROVED"));
    }

    @Test
    void approveRegistration_NonAdminFails() throws Exception {
        // Arrange
        Guard nonAdmin = new Guard(2L, "Guard User", "password123", "guard@test.com", "87654321");

        when(userService.findById(2L)).thenReturn(nonAdmin);
        when(shiftRegService.approveRegistration(1L, nonAdmin))
                .thenThrow(new IllegalArgumentException("Only admins can approve shift registrations"));

        // Act & Assert
        mockMvc.perform(post("/api/shiftRegistration/1/approve?adminId=2"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void rejectRegistration_Success() throws Exception {
        // Arrange
        Admin admin = new Admin(1L, "Admin User", "password123", "admin@test.com", "12345678");
        ShiftRegistration registration = new ShiftRegistration();
        registration.setRegistrationId(1L);
        registration.setRegistrationStatus(RegistrationStatus.REJECTED);

        when(userService.findById(1L)).thenReturn(admin);
        when(shiftRegService.rejectRegistration(1L, admin)).thenReturn(registration);

        // Act & Assert
        mockMvc.perform(post("/api/shiftRegistration/1/reject?adminId=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.regStatus").value("REJECTED"));
    }

    @Test
    void cancelRegistration_Success() throws Exception {
        // Arrange
        Guard guard = new Guard(2L, "Guard User", "password123", "guard@test.com", "87654321");
        ShiftRegistration registration = new ShiftRegistration();
        registration.setRegistrationId(1L);
        registration.setRegistrationStatus(RegistrationStatus.CANCELED);
        registration.setGuard(guard);

        when(userService.findById(2L)).thenReturn(guard);
        when(shiftRegService.cancelRegistration(1L, guard)).thenReturn(registration);

        // Act & Assert
        mockMvc.perform(post("/api/shiftRegistration/1/cancel?guardId=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.regStatus").value("CANCELLED"));
    }

    @Test
    void cancelRegistration_CannotCancelApproved() throws Exception {
        // Arrange
        Guard guard = new Guard(2L, "Guard User", "password123", "guard@test.com", "87654321");

        when(userService.findById(2L)).thenReturn(guard);
        when(shiftRegService.cancelRegistration(1L, guard))
                .thenThrow(new IllegalArgumentException("Only pending registrations can be cancelled"));

        // Act & Assert
        mockMvc.perform(post("/api/shiftRegistration/1/cancel?guardId=2"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void removeGuardFromShift_Success() throws Exception {
        // Arrange
        Admin admin = new Admin(1L, "Admin User", "password123", "admin@test.com", "12345678");
        ShiftRegistration registration = new ShiftRegistration();
        registration.setRegistrationId(1L);
        registration.setRegistrationStatus(RegistrationStatus.CANCELED);

        when(userService.findById(1L)).thenReturn(admin);
        when(shiftRegService.removeGuardFromShift(1L, admin)).thenReturn(registration);

        // Act & Assert
        mockMvc.perform(delete("/api/shiftRegistration/1/remove?adminId=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.regStatus").value("CANCELLED"));
    }

    @Test
    void getAllRegistrations() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/shiftRegistration"))
                .andExpect(status().isOk());
    }

    @Test
    void getRegistrationById_Success() throws Exception {
        // Arrange
        ShiftRegistration registration = new ShiftRegistration();
        registration.setRegistrationId(1L);
        registration.setRegistrationStatus(RegistrationStatus.PENDING);

        when(shiftRegService.findByRegId(1L)).thenReturn(registration);

        // Act & Assert
        mockMvc.perform(get("/api/shiftRegistration/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.registrationId").value(1L));
    }

    @Test
    void getRegistrationById_NotFound() throws Exception {
        // Arrange
        when(shiftRegService.findByRegId(999L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/shiftRegistration/999"))
                .andExpect(status().isOk());
    }

    @Test
    void updateRegistration_Success() throws Exception {
        // Arrange
        ShiftRegistration registration = new ShiftRegistration();
        registration.setRegistrationId(1L);
        registration.setRegistrationStatus(RegistrationStatus.APPROVED);

        when(shiftRegService.updateRegistration(any(ShiftRegistration.class)))
                .thenReturn(registration);

        // Act & Assert
        mockMvc.perform(put("/api/shiftRegistration")
                .contentType("application/json")
                .content("{\"registrationId\":1,\"regStatus\":\"APPROVED\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteRegistration_Success() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/shiftRegistration/1"))
                .andExpect(status().isOk());
    }
}

