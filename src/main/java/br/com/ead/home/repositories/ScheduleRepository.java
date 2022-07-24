package br.com.ead.home.repositories;

import br.com.ead.home.models.Appointment;
import br.com.ead.home.models.Shift;
import br.com.ead.home.models.primitives.ClinicianId;

import java.util.Set;

public interface ScheduleRepository {

    Set<Shift> findAllShiftsByClinicianId(ClinicianId clinicianId);

    Set<Appointment> findAllAppointmentByClinicianId(ClinicianId clinicianId);

    Set<Appointment> findAllAppointment();

    Set<Shift> findAllShift();
}
