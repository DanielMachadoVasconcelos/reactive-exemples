package br.com.ead.home.controllers;

import br.com.ead.home.configurations.system.ClockProvider;
import br.com.ead.home.configurations.system.MockSystemClockProvider;
import br.com.ead.home.models.api.TimeSlot;
import br.com.ead.home.models.primitives.ClinicianId;
import br.com.ead.home.services.BookablePreferenceService;
import br.com.ead.home.services.ScheduleAvailabilityService;
import br.com.ead.home.services.ScheduleService;
import br.com.ead.home.services.WorkScheduleService;
import br.com.ead.home.services.delegates.factories.ApplicationDelegateFactory;
import br.com.ead.home.services.implementations.AvailabilityService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.Set;

class ScheduleAvailabilityControllerTest {

    private ScheduleService scheduleService = ApplicationDelegateFactory.scheduleService();
    private WorkScheduleService shiftService = ApplicationDelegateFactory.workScheduleService();
    private BookablePreferenceService clinicianPreferencesService = ApplicationDelegateFactory.bookablePreferenceService();

    private ScheduleAvailabilityService availabilityService = new AvailabilityService(scheduleService, shiftService, clinicianPreferencesService);
    private ScheduleAvailabilityController classUnderTest = new ScheduleAvailabilityController(availabilityService);
    private ClockProvider clockProvider = new MockSystemClockProvider();

    @Test
    @DisplayName("Should find all bookable availabilities when requesting by clinicianId")
    void shouldReturnAllBookableAvailabilityWhenProvidedClinicianId() {

        // given: the expected clinicianId
        ClinicianId expectedClinicianId = new ClinicianId("Thomas");
        ZonedDateTime today = ZonedDateTime.now(clockProvider.currentSystemClock());

        // when: getting the clinician bookable availability
        Set<TimeSlot> availabilities =
                classUnderTest.getBookableAvailabilities(expectedClinicianId, today, today.plusDays(1));

        // then: some availability is found
        Assertions.assertAll(
            () -> Assertions.assertNotNull(availabilities, "Must not return null"),
            () -> Assertions.assertFalse(availabilities.isEmpty(), "Must return some availability")
        );
    }

    @Test
    @DisplayName("Should find no bookable availabilities when requesting with unknown clinicianId")
    void shouldFindNoBookableAvailabilityWhenRequestingWithUnknownClinicianId() {

        // given: the expected clinicianId
        ClinicianId expectedClinicianId = new ClinicianId("unknown");
        ZonedDateTime today = ZonedDateTime.now(clockProvider.currentSystemClock());

        // when: getting the clinician bookable availability
        Set<TimeSlot> availabilities =
                classUnderTest.getBookableAvailabilities(expectedClinicianId, today, today.plusDays(1));

        // then: some availability is found
        Assertions.assertAll(
                () -> Assertions.assertNotNull(availabilities, "Must not return null"),
                () -> Assertions.assertTrue(availabilities.isEmpty(), "Must return no availability")
        );
    }
}