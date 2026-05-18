package com.guardhub;


import com.guardhub.configuration.SecurityConfig;
import com.guardhub.shift.registration.RegistrationStatus;
import com.guardhub.shift.registration.ShiftRegistration;
import com.guardhub.shift.registration.ShiftRegistrationController;
import com.guardhub.shift.registration.ShiftRegistrationService;
import com.guardhub.user.Admin;
import com.guardhub.user.Guard;
import com.guardhub.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(ShiftRegistrationController.class)
@Import(SecurityConfig.class)
class ShiftRegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ShiftRegistrationService shiftRegService;

    @MockitoBean
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
        mockMvc.perform(post("/api/registrations")
                .contentType("application/json")
                .content("{\"registrationId\":1,\"registrationStatus\":\"PENDING\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.registrationStatus").value("PENDING"));
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
        mockMvc.perform(post("/api/registrations/1/approve?adminId=1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.registrationStatus").value("APPROVED"));
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
        mockMvc.perform(post("/api/registrations/1/reject?adminId=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.registrationStatus").value("REJECTED"));
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
        mockMvc.perform(post("/api/registrations/1/cancel?guardId=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.registrationStatus").value("CANCELED"));
    }

    @Test
    void cancelRegistration_CannotCancelApproved() throws Exception {
        // Arrange
        Guard guard = new Guard(2L, "Guard User", "password123", "guard@test.com", "87654321");

        when(userService.findById(2L)).thenReturn(guard);
        when(shiftRegService.cancelRegistration(1L, guard))
                .thenThrow(new IllegalArgumentException("Only pending registrations can be cancelled"));

        // Act & Assert
        mockMvc.perform(post("/api/registrations/1/cancel?guardId=2"))
                .andExpect(status().isBadRequest());
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
        mockMvc.perform(delete("/api/registrations/1/remove?adminId=1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getAllRegistrations() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/registrations"))
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
        mockMvc.perform(get("/api/registrations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.registrationId").value(1L));
    }

    @Test
    void getRegistrationById_NotFound() throws Exception {
        // Arrange
        when(shiftRegService.findByRegId(999L)).thenReturn(new ShiftRegistration());

        // Act & Assert
        mockMvc.perform(get("/api/registrations/999"))
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
        mockMvc.perform(put("/api/registrations")
                .contentType("application/json")
                .content("{\"registrationId\":1,\"registrationStatus\":\"APPROVED\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void deleteRegistration_Success() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/registrations/1"))
                .andExpect(status().isNoContent());
    }
}

