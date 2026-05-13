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

    private final LocalDateTime TEST_MONDAY = LocalDateTime.of(2030, 1, 7, 0, 0);

    @BeforeEach
    public void setUp() {

        City city = new City("Copenhagen");
        cityRepository.save(city);

        Client client = new Client(
                "ExampleRoad, ExampleCity, ExampleCountry",
                city,
                "a@example.com",
                null,
                "ExampleCompany"
        );
        clientRepository.save(client);

        Guard guard1 = new Guard(null, "Jorn", "abcdefg", "jorn@example.com", "12345678");
        Guard guard2 = new Guard(null, "Jorno", "gefbc", "jorno@example.com", "87654321");
        savedGuards = userRepository.saveAll(List.of(guard1, guard2));

        Shift shift1 = new Shift(null, "example title", client, "example description", 4, TEST_MONDAY.plusDays(3).withHour(8), TEST_MONDAY.plusDays(3).withHour(16));
        Shift shift2 = new Shift(null, "example titl3", client, "3xample description", 2, TEST_MONDAY.plusDays(3).withHour(8), TEST_MONDAY.plusDays(3).withHour(16));
        Shift shift3 = new Shift(null, "ex4mple titl3", client, "3x4mpl3 descripti0n", 3, TEST_MONDAY.plusDays(4).withHour(8), TEST_MONDAY.plusDays(4).withHour(16));
        Shift shift4 = new Shift(null, "ex4mpl3 titl3", client, "3x4mpl3 d3scripti0n", 9, TEST_MONDAY.plusDays(4).withHour(8), TEST_MONDAY.plusDays(4).withHour(16));
        Shift shift5 = new Shift(null, "ex4mpl3 titl3", client, "3x4mpl3 d3scripti0n", 1, TEST_MONDAY.plusDays(5).withHour(8), TEST_MONDAY.plusDays(5).withHour(16));

        // Outside the week: should not appear in results
        Shift shift6 = new Shift(null, "3x4mpl3 t1tl3", client, "3x4mpl3 d3scr1pti0n", 10, TEST_MONDAY.plusDays(12).withHour(8), TEST_MONDAY.plusDays(12).withHour(16));

        savedShifts = shiftRepository.saveAll(
                List.of(shift1, shift2, shift3, shift4, shift5, shift6)
        );

        ShiftRegistration r1 = new ShiftRegistration(null, guard1, shift1, RegistrationStatus.APPROVED);
        ShiftRegistration r2 = new ShiftRegistration(null, guard1, shift2, RegistrationStatus.PENDING);
        ShiftRegistration r3 = new ShiftRegistration(null, guard1, shift3, RegistrationStatus.PENDING);
        ShiftRegistration r4 = new ShiftRegistration(null, guard2, shift1, RegistrationStatus.PENDING);

        shiftRegistrationRepository.saveAll(List.of(r1, r2, r3, r4));
    }

    @Test
    public void findAllForWeekReturnsAllShiftsForWeekButOnlyShiftsInsideWeek() {
        List<ShiftDTO> results =
                shiftRepository.findAllForWeek(TEST_MONDAY, TEST_MONDAY.plusDays(7));

        Assertions.assertEquals(5, results.size());

        Long outsideId = savedShifts.get(5).getShiftId();
        List<Long> resultIds = results.stream().map(ShiftDTO::id).toList();

        Assertions.assertFalse(resultIds.contains(outsideId));
    }

    @Test
    public void findAllForGuardForWeekReturnsOnlyGuardShiftsInsideWeek() {
        Long guardId = savedGuards.getFirst().getUserId();

        List<ShiftDTO> results =
                shiftRepository.findAllForGuardForWeek(guardId, TEST_MONDAY, TEST_MONDAY.plusDays(7));

        Assertions.assertEquals(3, results.size());

        List<Long> guardShiftIds = shiftRegistrationRepository.findAll().stream()
                .filter(r -> r.getGuard().getUserId().equals(guardId))
                .map(r -> r.getShift().getShiftId())
                .toList();

        Assertions.assertTrue(
                results.stream().allMatch(s -> guardShiftIds.contains(s.id()))
        );
    }
}