package br.com.ead.home.repositories;

import br.com.ead.home.models.Appointment;
import br.com.ead.home.models.Shift;
import br.com.ead.home.models.Slot;
import br.com.ead.home.models.api.TimeSlot;
import br.com.ead.home.models.primitives.ClinicianId;
import br.com.ead.home.models.primitives.PatientId;
import org.apache.commons.collections4.SetUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MockScheduleRepository implements ScheduleRepository {

    private static final Random random = new Random();

    @Override
    public Set<Shift> findAllByClinicianId(ClinicianId clinicianId) {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.of(8, 0, 0);
        ZonedDateTime seed = ZonedDateTime.of(today, now, ZoneOffset.UTC);

        Predicate<ZonedDateTime> hasNext = item -> seed.plusDays(7).isAfter(item);
        UnaryOperator<ZonedDateTime> next = item -> item.plusDays(1);

       return Stream.iterate(seed, hasNext, next)
               .map(date -> (TimeSlot) new Slot(date, date.plusHours(8)))
               .map(timeSlot -> new Shift(clinicianId, timeSlot))
               .collect(Collectors.toSet());
    }

    @Override
    public Set<Appointment> findAllAppointmentByClinicianId(ClinicianId clinicianId) {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.of(8, 0, 0);
        ZonedDateTime seed = ZonedDateTime.of(today, now, ZoneOffset.UTC);

        Predicate<ZonedDateTime> hasNext = item -> seed.plusDays(7).isAfter(item);
        UnaryOperator<ZonedDateTime> next = item -> item.plusHours(1);

        return Stream.iterate(seed, hasNext, next)
                .map(date -> (TimeSlot) new Slot(date, date.plusHours(1)))
                .filter(any -> random.nextBoolean() && random.nextBoolean() && random.nextBoolean())
                .map(timeSlot -> new Appointment(clinicianId, generatePatientId(), timeSlot))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Shift> findAllShift() {
        return SetUtils.union(findAllByClinicianId(new ClinicianId("Thomas")),
                              findAllByClinicianId(new ClinicianId("Sara")));
    }


    private static PatientId generatePatientId() {
        return random.nextBoolean() ? new PatientId("Sara") : new PatientId("Daniel");
    }

    public static ZonedDateTime time(String time) {
        return time(LocalDate.now(), time);
    }

    public static ZonedDateTime time(LocalDate date, String time) {
        LocalTime now = LocalTime.parse(time);
        return ZonedDateTime.of(date, now, ZoneOffset.UTC);
    }
}
