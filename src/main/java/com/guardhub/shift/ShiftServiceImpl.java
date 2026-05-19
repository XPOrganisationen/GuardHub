package com.guardhub.shift;

import com.guardhub.client.Client;
import com.guardhub.client.ClientRepository;
import com.guardhub.exceptions.EntityDoesNotExistException;
import com.guardhub.user.UserRepository;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ShiftServiceImpl implements ShiftService {
    private final ShiftRepository shiftRepository;
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;

    public ShiftServiceImpl(ShiftRepository shiftRepository, UserRepository userRepository, ClientRepository clientRepository) {
        this.shiftRepository = shiftRepository;
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public Map<DayOfWeek, List<ShiftDTO>> findAllForWeek(Long weekOffset) {
        Pair<LocalDateTime, LocalDateTime> startEnd = weekOffsetToWeekLimits(weekOffset);
        List<ShiftDTO> shifts = shiftRepository.findAllForWeek(startEnd.getFirst(), startEnd.getSecond());
        return sortShiftResults(shifts);
    }

    @Override
    public Map<DayOfWeek, List<ShiftDTO>> findAllForGuardForWeek(Long guardId, Long weekOffset) {
        if (!userRepository.existsById(guardId)) {
            throw new EntityDoesNotExistException("No guard with ID " + guardId + " exists");
        }

        Pair<LocalDateTime, LocalDateTime> startEnd = weekOffsetToWeekLimits(weekOffset);
        List<ShiftDTO> shifts = shiftRepository.findAllForGuardForWeek(guardId, startEnd.getFirst(), startEnd.getSecond());
        return sortShiftResults(shifts);
    }

    /*
        Sorting deserves an explanation:
        Done here because using a native (MySQL) query would make the code less flexible
        if our customer should ever choose a DB-system different from MySQL, and sorting
        is a nightmare with complex comparators and joins in JPQL.

        Also, getDayOfWeek() returns DayOfWeek enum options (strings and integers)
        with integers in the range 1 = Monday to 7 = Sunday.
     */
    private Map<DayOfWeek, List<ShiftDTO>> sortShiftResults(List<ShiftDTO> shifts) {
        Comparator<ShiftDTO> availableSpots = Comparator.comparing(s -> s.requiredGuards() - s.registrations());
        shifts.sort(availableSpots.reversed().thenComparing(ShiftDTO::shiftStart));
        return shifts
                .stream()
                .collect(Collectors.groupingBy(shift -> shift.shiftStart().getDayOfWeek()));
    }

    private Pair<LocalDateTime, LocalDateTime> weekOffsetToWeekLimits(Long weekOffset) {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime monday = today.with(
                TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)
        ).plusWeeks(weekOffset);

        LocalDateTime weekEnd = monday.plusDays(7);
        return Pair.of(monday, weekEnd);
    }

    @Override
    public ShiftDTO createShift(CreateShiftRequest request) {
        Client client = clientRepository.findById(request.clientId())
                .orElseThrow(() -> new EntityDoesNotExistException("No client with ID " + request.clientId() + " exists"));

        Shift shift = new Shift(
                null,
                request.title(),
                client,
                request.description(),
                request.requiredGuardAmount(),
                request.shiftStart(),
                request.shiftEnd(),
                request.foodServed(),
                request.parkingInstructions()
        );

        Shift saved = shiftRepository.save(shift);

        return new ShiftDTO(
                saved.getShiftId(),
                saved.getTitle(),
                client.getName(),
                saved.getShiftStart(),
                saved.getShiftEnd(),
                saved.getDescription(),
                client.getAddress(),
                saved.isFoodServed(),
                client.getPhoneNumber(),
                saved.getParkingInstructions(),
                saved.getRequiredGuardAmount(),
                0L
        );
    }

    @Override
    public void deleteShift(Long shiftId) {
        if (!shiftRepository.existsById(shiftId)) {
            throw new EntityDoesNotExistException("No shift with ID " + shiftId + " exists");
        }
        shiftRepository.deleteById(shiftId);
    }

    @Override
    public ShiftDTO updateShift(Long shiftId, UpdateShiftRequest request) {
        Shift shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new EntityDoesNotExistException("No shift with ID " + shiftId + " exists"));

        shift.setTitle(request.title());
        shift.setShiftStart(request.shiftStart());
        shift.setShiftEnd(request.shiftEnd());
        shift.setDescription(request.description());
        shift.setFoodServed(request.foodServed());
        shift.setParkingInstructions(request.parkingInstructions());
        shift.setRequiredGuardAmount(request.requiredGuardAmount());

        Shift saved = shiftRepository.save(shift);
        Client client = saved.getClient();

        return new ShiftDTO(
                saved.getShiftId(),
                saved.getTitle(),
                client.getName(),
                saved.getShiftStart(),
                saved.getShiftEnd(),
                saved.getDescription(),
                client.getAddress(),
                saved.isFoodServed(),
                client.getPhoneNumber(),
                saved.getParkingInstructions(),
                saved.getRequiredGuardAmount(),
                0L
        );
    }
}
