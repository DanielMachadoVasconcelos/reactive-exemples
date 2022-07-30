package br.com.ead.home.services.delegates.lookups;

import br.com.ead.home.common.context.InitialContext;
import br.com.ead.home.common.lookups.InMemoryLookup;
import br.com.ead.home.common.namespace.NamespaceResolver;
import br.com.ead.home.common.types.PartitionType;
import br.com.ead.home.common.types.StageType;
import br.com.ead.home.configurations.Environment;
import br.com.ead.home.services.ScheduleService;
import lombok.extern.log4j.Log4j2;

import java.util.Optional;

@Log4j2
public record ScheduleServiceLookup(
        Environment environment,
        InitialContext initialContext,
        NamespaceResolver resolver
) implements InMemoryLookup<ScheduleService> {

    @Override
    public ScheduleService lookup() {
        StageType stage = environment.getStage();
        PartitionType partition = environment.getPartition();

        String jndiName = resolver.namespace(stage, partition, ScheduleService.class.getName());
        ScheduleService service = (ScheduleService) initialContext.lookup(jndiName);
        log.debug("Looking up for sSchedule service. Using Stage %s and Partition %s bean".formatted(stage, partition));
        return Optional.ofNullable(service)
        .orElseThrow(() -> new IllegalStateException("No bean register in context for class '%s' ".formatted(ScheduleService.class.getName())));
    }
}
