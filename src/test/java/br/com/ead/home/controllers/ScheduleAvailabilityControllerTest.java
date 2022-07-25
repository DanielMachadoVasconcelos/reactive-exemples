package br.com.ead.home.controllers;

import br.com.ead.home.configurations.SystemClockProvider;
import br.com.ead.home.models.api.TimeSlot;
import br.com.ead.home.models.primitives.ClinicianId;
import br.com.ead.home.repositories.AppointmentRepository;
import br.com.ead.home.repositories.ClinicianPreferencesRepository;
import br.com.ead.home.repositories.MockAppointmentRepository;
import br.com.ead.home.repositories.MockClinicianPreferencesRepository;
import br.com.ead.home.services.AppointmentService;
import br.com.ead.home.services.AvailabilityService;
import br.com.ead.home.services.ClinicianPreferencesService;
import br.com.ead.home.services.api.BookablePreferenceService;
import br.com.ead.home.services.api.ScheduleAvailabilityService;
import br.com.ead.home.services.api.ScheduleService;
import br.com.ead.home.services.api.WorkScheduleService;
import br.com.ead.home.services.delegates.factories.WorkScheduleDelegateFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Set;

class ScheduleAvailabilityControllerTest {

    private static final LocalDate today = LocalDate.now();
    private static final LocalTime eight = LocalTime.of(8,0 ,0);

    private static final Instant virtualToday = ZonedDateTime.of(today, eight, ZoneOffset.UTC).toInstant();
    private static final SystemClockProvider systemClockProvider = () -> Clock.fixed(virtualToday, ZoneOffset.UTC);

    private AppointmentRepository appointmentRepository = new MockAppointmentRepository(systemClockProvider);
    private ClinicianPreferencesRepository clinicianPreferencesRepository = new MockClinicianPreferencesRepository(systemClockProvider);

    private ScheduleService scheduleService = new AppointmentService(appointmentRepository);
    private WorkScheduleService shiftService = WorkScheduleDelegateFactory.createWorkScheduleService();
    private BookablePreferenceService clinicianPreferencesService = new ClinicianPreferencesService(clinicianPreferencesRepository);

    private ScheduleAvailabilityService availabilityService = new AvailabilityService(scheduleService, shiftService, clinicianPreferencesService);
    private ScheduleAvailabilityController classUnderTest = new ScheduleAvailabilityController(availabilityService);

    @Test
    @DisplayName("Should find all bookable availabilities when requesting by clinicianId")
    void shouldReturnAllBookableAvailabilityWhenProvidedClinicianId() {

        // given: the expected clinicianId
        ClinicianId expectedClinicianId = new ClinicianId("Thomas");
        ZonedDateTime today = ZonedDateTime.now(systemClockProvider.currentSystemClock());

        // when: getting the clinician bookable availability
        Set<TimeSlot> availabilities =
                classUnderTest.getBookableAvailabilities(expectedClinicianId, today, today.plusDays(1));

        // then: some availability is found
        Assertions.assertAll(
            () -> Assertions.assertNotNull(availabilities, "Must not return null"),
            () -> Assertions.assertFalse(availabilities.isEmpty(), "Must return some availability")
        );
    }
}