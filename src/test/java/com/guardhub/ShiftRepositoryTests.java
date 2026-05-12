package com.guardhub;

import com.guardhub.city.City;
import com.guardhub.city.CityRepository;
import com.guardhub.client.Client;
import com.guardhub.client.ClientRepository;
import com.guardhub.shift.Shift;
import com.guardhub.shift.ShiftDTO;
import com.guardhub.shift.ShiftRepository;
import com.guardhub.shift.registration.RegistrationStatus;
import com.guardhub.shift.registration.ShiftRegistration;
import com.guardhub.shift.registration.ShiftRegistrationRepository;
import com.guardhub.user.Guard;
import com.guardhub.user.User;
import com.guardhub.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
public class ShiftRepositoryTests {
    @Autowired
    private ShiftRepository shiftRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShiftRegistrationRepository shiftRegistrationRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CityRepository cityRepository;

    private List<Shift> savedShifts;

    private List<User> savedGuards;

    @BeforeEach
    public void setUp() {
        /* The null IDs may seem odd, but JPA may choose not to start from one or
            to stick to any convention for initializing IDs, so you need to store them
            and refer to the IDs from the DB later to get expected results.
         */
        City city = new City("Copenhagen");
        cityRepository.save(city);

        Client client = new Client("ExampleRoad, ExampleCity, ExampleCountry", city, "a@example.com", null, "ExampleCompany");
        clientRepository.save(client);

        Guard guard1 = new Guard(null, "Jorn", "abcdefg", "jorn@example.com", "12345678");
        Guard guard2 = new Guard(null, "Jorno", "gefbc", "jorno@example.com", "87654321");
        savedGuards = userRepository.saveAll(List.of(guard1, guard2));

        Shift shift1 = new Shift(null, "example title", client, "example description", 4, LocalDateTime.of(2026, 5, 14, 8, 0, 0), LocalDateTime.of(2026, 5, 14, 16, 0, 0));
        Shift shift2 = new Shift(null, "example titl3", client, "3xample description", 2, LocalDateTime.of(2026, 5, 14, 8, 0, 0), LocalDateTime.of(2026, 5, 14, 16, 0, 0));
        Shift shift3 = new Shift(null, "ex4mple titl3", client, "3x4mpl3 descripti0n", 3, LocalDateTime.of(2026, 5, 15, 8, 0, 0), LocalDateTime.of(2026, 5, 15, 16, 0, 0));
        Shift shift4 = new Shift(null, "ex4mpl3 titl3", client, "3x4mpl3 d3scripti0n", 9, LocalDateTime.of(2026, 5, 15, 8, 0, 0), LocalDateTime.of(2026, 5, 15, 16, 0, 0));
        Shift shift5 = new Shift(null, "ex4mpl3 titl3", client, "3x4mpl3 d3scripti0n", 1, LocalDateTime.of(2026, 5, 16, 8, 0, 0), LocalDateTime.of(2026, 5, 16, 16, 0, 0));
        savedShifts = shiftRepository.saveAll(List.of(shift1, shift2, shift3, shift4, shift5));

        ShiftRegistration shiftRegistration1 = new ShiftRegistration(null, guard1, shift1, RegistrationStatus.APPROVED);
        ShiftRegistration shiftRegistration2 = new ShiftRegistration(null, guard1, shift2, RegistrationStatus.PENDING);
        ShiftRegistration shiftRegistration3 = new ShiftRegistration(null, guard1, shift3, RegistrationStatus.PENDING);
        ShiftRegistration shiftRegistration4 = new ShiftRegistration(null, guard2, shift1, RegistrationStatus.PENDING);
        shiftRegistrationRepository.saveAll(List.of(shiftRegistration1, shiftRegistration2, shiftRegistration3, shiftRegistration4));
    }

    @Test
    public void findAllForWeekReturnsAMapOfDaysToListsSortedByCapacityThenStartTime() {
        var firstShiftStart = savedShifts.getFirst().getShiftStart();
        List<ShiftDTO> allShiftsForWeek = shiftRepository.findAllForWeek(firstShiftStart, firstShiftStart.plusWeeks(1));
        Assertions.assertEquals(5, allShiftsForWeek.size());

        for (int i = 0; i < savedShifts.size(); i++) {
            Assertions.assertEquals(savedShifts.get(i).getTitle(), allShiftsForWeek.get(i).title());
        }
    }

    @Test
    public void findAllForGuardForWeekReturnsAMapOfDaysToListsSortedByCapacityThenStartTimeForOnlyOneGuard() {
        var firstShiftStart = savedShifts.getFirst().getShiftStart();
        var expectedIds = List.of(savedShifts.getFirst().getShiftId(), savedShifts.get(1).getShiftId(), savedShifts.get(2).getShiftId());
        List<ShiftDTO> allShiftsForGuardWithId1 = shiftRepository.findAllForGuardForWeek(savedGuards.getFirst().getUserId(), firstShiftStart, firstShiftStart.plusWeeks(1));
        Assertions.assertEquals(3, allShiftsForGuardWithId1.size());
        Assertions.assertEquals(allShiftsForGuardWithId1.stream().map(ShiftDTO::id).toList(), expectedIds);
    }
}
