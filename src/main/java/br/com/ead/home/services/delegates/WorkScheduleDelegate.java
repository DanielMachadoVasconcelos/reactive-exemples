package br.com.ead.home.services.delegates;

import br.com.ead.home.models.Shift;
import br.com.ead.home.models.primitives.ClinicianId;
import br.com.ead.home.services.api.WorkScheduleService;
import br.com.ead.home.services.delegates.lookups.WorkScheduleLookup;
import br.com.ead.home.services.delegates.types.ServicePartitionType;
import br.com.ead.home.services.delegates.types.ServiceStageType;

import java.util.Set;

public record WorkScheduleDelegate(ServiceStageType stage, ServicePartitionType partition) implements WorkScheduleService {

    private static final WorkScheduleLookup lookup = new WorkScheduleLookup();

    @Override
    public Set<Shift> findAllByClinicianId(ClinicianId clinicianId) {
        return lookup.getService(stage, partition).findAllByClinicianId(clinicianId);
    }

    @Override
    public Set<Shift> findAllShifts() {
        return lookup.getService(stage, partition).findAllShifts();
    }
}
