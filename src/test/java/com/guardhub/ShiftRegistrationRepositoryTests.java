package com.guardhub;

import com.guardhub.city.CityRepository;
import com.guardhub.client.ClientRepository;
import com.guardhub.exceptions.EntityDoesNotExistException;
import com.guardhub.city.City;
import com.guardhub.client.Client;
import com.guardhub.shift.Shift;
import com.guardhub.shift.ShiftRepository;
import com.guardhub.shift.registration.RegistrationStatus;
import com.guardhub.shift.registration.ShiftRegistration;
import com.guardhub.shift.registration.ShiftRegistrationRepository;
import com.guardhub.user.Guard;
import com.guardhub.user.User;
import com.guardhub.user.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
public class ShiftRegistrationRepositoryTests {
    @Autowired
    private ShiftRegistrationRepository shiftRegistrationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShiftRepository shiftRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private EntityManager em;

    private List<Guard> savedGuards = List.of();
    private List<Shift> savedShifts = List.of();
    private List<ShiftRegistration> savedRegistrations = List.of();

    private final LocalDateTime TEST_MONDAY = LocalDateTime.of(2030, 1, 7, 0, 0);

    @Autowired
    private CityRepository cityRepository;

    @BeforeEach
    public void setUp() {
        City city = new City("Copenhagen");
        cityRepository.save(city);

        Client client = new Client("ExampleRoad, ExampleCity, ExampleCountry", city, "a@example.com", null, "ExampleCompany", "12345678");
        clientRepository.save(client);

        Guard guard1 = new Guard(null, "Jorn", "abcdefg", "jorn@example.com", "12345678");
        Guard guard2 = new Guard(null, "Jorno", "gefbc", "jorno@example.com", "87654321");
        Guard guard3 = new Guard(null, "Jorna", "ee0efc", "jorna@example.com", "13245768");
        Guard guard4 = new Guard(null, "Jornu", "a0b1c2", "jornu@example.com", "86754231");

        savedGuards = userRepository.saveAll(List.of(guard1, guard2, guard3, guard4));

        Shift shift1 = new Shift(null, "example title", client, "example description", 4, TEST_MONDAY.plusDays(3).withHour(8), TEST_MONDAY.plusDays(3).withHour(16), false, "park");
        Shift shift2 = new Shift(null, "example titl3", client, "3xample description", 2, TEST_MONDAY.plusDays(3).withHour(8), TEST_MONDAY.plusDays(3).withHour(16), false, "no park");
        Shift shift3 = new Shift(null, "ex4mple titl3", client, "3x4mpl3 descripti0n", 3, TEST_MONDAY.plusDays(4).withHour(8), TEST_MONDAY.plusDays(4).withHour(16), true, "park");
        Shift shift4 = new Shift(null, "ex4mpl3 titl3", client, "3x4mpl3 d3scripti0n", 9, TEST_MONDAY.plusDays(4).withHour(8), TEST_MONDAY.plusDays(4).withHour(16), false, "no park");
        Shift shift5 = new Shift(null, "ex4mpl3 titl3", client, "3x4mpl3 d3scripti0n", 1, TEST_MONDAY.plusDays(5).withHour(8), TEST_MONDAY.plusDays(5).withHour(16), true, "park");

        // Outside the week: should not appear in results
        Shift shift6 = new Shift(null, "3x4mpl3 t1tl3", client, "3x4mpl3 d3scr1pti0n", 10, TEST_MONDAY.plusDays(12).withHour(8), TEST_MONDAY.plusDays(12).withHour(16), true, "no park");

        savedShifts = shiftRepository.saveAll(
                List.of(shift1, shift2, shift3, shift4, shift5, shift6)
        );

        ShiftRegistration shiftRegistration1 = new ShiftRegistration(null, guard1, shift1, RegistrationStatus.APPROVED);
        ShiftRegistration shiftRegistration2 = new ShiftRegistration(null, guard1, shift2, RegistrationStatus.PENDING);
        ShiftRegistration shiftRegistration3 = new ShiftRegistration(null, guard1, shift3, RegistrationStatus.PENDING);
        ShiftRegistration shiftRegistration4 = new ShiftRegistration(null, guard2, shift1, RegistrationStatus.PENDING);
        ShiftRegistration shiftRegistration5 = new ShiftRegistration(null, guard2, shift3, RegistrationStatus.PENDING);
        ShiftRegistration shiftRegistration6 = new ShiftRegistration(null, guard2, shift4, RegistrationStatus.PENDING);
        ShiftRegistration shiftRegistration7 = new ShiftRegistration(null, guard3, shift5, RegistrationStatus.PENDING);
        ShiftRegistration shiftRegistration8 = new ShiftRegistration(null, guard4, shift1, RegistrationStatus.APPROVED);
        ShiftRegistration shiftRegistration9 = new ShiftRegistration(null, guard4, shift4, RegistrationStatus.PENDING);
        ShiftRegistration shiftRegistration10 = new ShiftRegistration(null, guard4, shift1, RegistrationStatus.PENDING);
        ShiftRegistration shiftRegistration11 = new ShiftRegistration(null, guard1, shift4, RegistrationStatus.PENDING);

        List<ShiftRegistration> allRegistrations = List.of(
                shiftRegistration1, shiftRegistration2, shiftRegistration3, shiftRegistration4,
                shiftRegistration5, shiftRegistration6, shiftRegistration7, shiftRegistration8,
                shiftRegistration9, shiftRegistration10, shiftRegistration11
        );

        savedRegistrations = shiftRegistrationRepository.saveAll(allRegistrations);
    }

    @Test
    public void findAcceptedGuardsFindsAllGuardsApprovedForAShift() {
        List<String> expectedNames = List.of("Jorn", "Jornu");
        Assertions.assertEquals(expectedNames, shiftRegistrationRepository.findAcceptedGuardsByShiftId(savedShifts.getFirst().getShiftId()).stream().map(User::getName).toList());
    }

    @Test
    public void findAcceptedGuardsFindsNoGuardsIfNoneApprovedForAShift() {
        Assertions.assertEquals(List.of(), shiftRegistrationRepository.findAcceptedGuardsByShiftId(savedShifts.get(1).getShiftId()));
    }

    @Test
    public void guardHasRegistrationReturnsTrueIfGuardHasARegistrationForAShift() {
        Assertions.assertTrue(shiftRegistrationRepository.guardHasRegistrationForShift(savedGuards.getFirst().getUserId(), savedShifts.getFirst().getShiftId()));
    }

    @Test
    public void guardHasRegistrationReturnsFalseIfGuardHasNoRegistrationForAShift() {
        Assertions.assertFalse(shiftRegistrationRepository.guardHasRegistrationForShift(savedGuards.getFirst().getUserId(), savedShifts.get(4).getShiftId()));
    }

    @Test
    public void deleteShiftRegistrationByShiftAndGuardDeletesRegistrationIfExists() {
        User guard1 = userRepository.findById(savedGuards.getFirst().getUserId()).orElseThrow(() -> new EntityDoesNotExistException("No user with ID " + 1L + " exists"));
        Shift shift1 = shiftRepository.findById(savedShifts.getFirst().getShiftId()).orElseThrow(() -> new EntityDoesNotExistException("No shift with ID " + 1L + " exists"));
        Assertions.assertTrue(shiftRegistrationRepository.findById(savedRegistrations.getFirst().getRegistrationId()).isPresent());
        Long previousRegistrationId = savedRegistrations.getFirst().getRegistrationId();
        shiftRegistrationRepository.deleteByShiftIdAndGuardId(shift1.getShiftId(), guard1.getUserId());
        shiftRegistrationRepository.flush();
        em.clear(); // WEIRD SYNC BUG, DO NOT REMOVE
        Assertions.assertFalse(shiftRegistrationRepository.findById(previousRegistrationId).isPresent());
    }
}
