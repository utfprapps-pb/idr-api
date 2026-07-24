package br.gov.pr.idr.application.property_management.sync.upload;

import br.gov.pr.idr.application.shared.stereotype.CommandUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.property_management.sync.context.SyncContext;
import br.gov.pr.idr.domain.property_management.sync.entity.SyncEntityHandler;
import br.gov.pr.idr.domain.property_management.sync.entity.SyncEntityResult;
import br.gov.pr.idr.domain.property_management.sync.mapping.SyncIdMappingGateway;
import br.gov.pr.idr.domain.property_management.sync.vo.OfflineEntityType;
import br.gov.pr.idr.domain.property_management.sync.vo.SyncEntityStatus;
import br.gov.pr.idr.domain.shared.tactical.exceptions.DomainException;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CommandUseCase
public class UploadSyncUseCase extends UseCase<UploadSyncCommand, List<SyncEntityResult>> {

    private static final int MAX_BATCH_SIZE = 100;

    private final SyncIdMappingGateway idMappingGateway;
    private final Map<OfflineEntityType, SyncEntityHandler> handlersByType;
    private final List<OfflineEntityType> processingOrder;

    public UploadSyncUseCase(final List<SyncEntityHandler> handlers,
                             final SyncIdMappingGateway idMappingGateway) {
        this.idMappingGateway = idMappingGateway;
        this.handlersByType = this.indexByType(handlers);
        this.processingOrder = this.topologicalOrder(this.handlersByType);
    }

    @Override
    public List<SyncEntityResult> execute(final UploadSyncCommand command) {
        if (command.entities().size() > MAX_BATCH_SIZE) {
            throw DomainException.from("Batch não pode exceder " + MAX_BATCH_SIZE + " entidades");
        }

        final var context = new SyncContext(command.technicianId(), idMappingGateway);
        final var byType = command.entities().stream()
                .collect(Collectors.groupingBy(OfflineEntityCommand::type));

        final List<SyncEntityResult> results = new ArrayList<>();

        for (final var type : processingOrder) {
            for (final var entity : byType.getOrDefault(type, List.of())) {
                results.add(safeHandle(entity, context));
            }
        }

        for (final var entity : command.entities()) {
            if (!handlersByType.containsKey(entity.type())) {
                results.add(new SyncEntityResult(entity.localId(), null, SyncEntityStatus.FAILED,
                                                 "Tipo não suportado no sync: " + entity.type()));
            }
        }

        return results;
    }

    private SyncEntityResult safeHandle(final OfflineEntityCommand entity, final SyncContext context) {
        final var handler = handlersByType.get(entity.type());
        try {
            return handler.handle(entity, context);
        } catch (final RuntimeException e) {
            return new SyncEntityResult(entity.localId(), null, SyncEntityStatus.FAILED, e.getMessage());
        }
    }

    private Map<OfflineEntityType, SyncEntityHandler> indexByType(final List<SyncEntityHandler> handlers) {
        final Map<OfflineEntityType, SyncEntityHandler> index = new EnumMap<>(OfflineEntityType.class);
        for (final var handler : handlers) {
            final var previous = index.putIfAbsent(handler.type(), handler);
            if (previous != null) {
                throw DomainException.from("Handler duplicado para o tipo " + handler.type() + ": "
                        + previous.getClass().getSimpleName() + " e " + handler.getClass().getSimpleName());
            }
        }
        return index;
    }

    private List<OfflineEntityType> topologicalOrder(final Map<OfflineEntityType, SyncEntityHandler> handlersByType) {
        final Map<OfflineEntityType, Integer> inDegree = new EnumMap<>(OfflineEntityType.class);
        final Map<OfflineEntityType, List<OfflineEntityType>> dependents = new EnumMap<>(OfflineEntityType.class);
        for (final var type : handlersByType.keySet()) {
            inDegree.put(type, 0);
            dependents.put(type, new ArrayList<>());
        }
        this.buildDependencyGraph(handlersByType, inDegree, dependents);

        final List<OfflineEntityType> order = this.kahnSort(inDegree, dependents);

        if (order.size() != handlersByType.size()) {
            throw DomainException.from("Dependência cíclica entre SyncEntityHandlers");
        }
        return order;
    }

    private void buildDependencyGraph(final Map<OfflineEntityType, SyncEntityHandler> handlersByType,
                                              final Map<OfflineEntityType, Integer> inDegree,
                                              final Map<OfflineEntityType, List<OfflineEntityType>> dependents) {
        for (final var handler : handlersByType.values()) {
            for (final var dependency : handler.dependencies()) {
                if (!handlersByType.containsKey(dependency)) {
                    continue;
                }
                dependents.get(dependency).add(handler.type());
                inDegree.merge(handler.type(), 1, Integer::sum);
            }
        }
    }

    private List<OfflineEntityType> kahnSort(final Map<OfflineEntityType, Integer> inDegree,
                                                     final Map<OfflineEntityType, List<OfflineEntityType>> dependents) {
        final Deque<OfflineEntityType> ready = new ArrayDeque<>();
        for (final var entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                ready.add(entry.getKey());
            }
        }

        final List<OfflineEntityType> order = new ArrayList<>();
        while (!ready.isEmpty()) {
            final var type = ready.poll();
            order.add(type);
            for (final var dependent : dependents.get(type)) {
                if (inDegree.merge(dependent, -1, Integer::sum) == 0) {
                    ready.add(dependent);
                }
            }
        }
        return order;
    }
}
