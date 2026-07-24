package br.gov.pr.idr.application.property_management.sync;

import br.gov.pr.idr.application.property_management.sync.upload.*;
import br.gov.pr.idr.domain.iam.user.UserID;
import br.gov.pr.idr.domain.property_management.sync.context.SyncContext;
import br.gov.pr.idr.domain.property_management.sync.entity.OfflineEntityCommand;
import br.gov.pr.idr.domain.property_management.sync.entity.SyncEntityHandler;
import br.gov.pr.idr.domain.property_management.sync.entity.SyncEntityResult;
import br.gov.pr.idr.domain.property_management.sync.mapping.SyncIdMappingGateway;
import br.gov.pr.idr.domain.property_management.sync.vo.OfflineEntityType;
import br.gov.pr.idr.domain.property_management.sync.vo.SyncEntityStatus;
import br.gov.pr.idr.domain.shared.tactical.exceptions.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@DisplayName("UploadSyncUseCase (orquestração genérica)")
class UploadSyncUseCaseTest {

    private final SyncIdMappingGateway idMappingGateway = mock(SyncIdMappingGateway.class);

    @Test
    @DisplayName("deve processar dependências antes dos dependentes independente da ordem no payload")
    void shouldProcessDependenciesBeforeDependents() {
        final var processingOrder = new ArrayList<OfflineEntityType>();
        final var useCase = new UploadSyncUseCase(
                List.of(
                        recordingHandler(OfflineEntityType.PROPERTY, Set.of(OfflineEntityType.PRODUCER), processingOrder),
                        recordingHandler(OfflineEntityType.PRODUCER, Set.of(), processingOrder)
                ),
                idMappingGateway);

        final var command = command(
                entity(OfflineEntityType.PROPERTY),
                entity(OfflineEntityType.PRODUCER));

        useCase.execute(command);

        assertEquals(List.of(OfflineEntityType.PRODUCER, OfflineEntityType.PROPERTY), processingOrder);
    }

    @Test
    @DisplayName("deve retornar FAILED para tipo sem handler sem abortar o restante do batch")
    void shouldReturnFailedForTypeWithoutHandler() {
        final var useCase = new UploadSyncUseCase(
                List.of(handler(OfflineEntityType.PRODUCER, Set.of(), cmd -> ok(cmd))),
                idMappingGateway);

        final var producer = entity(OfflineEntityType.PRODUCER);
        final var property = entity(OfflineEntityType.PROPERTY);
        final var results = useCase.execute(command(producer, property));

        assertEquals(2, results.size());
        assertEquals(SyncEntityStatus.CREATED, resultOf(results, producer.localId()).status());
        final var failed = resultOf(results, property.localId());
        assertEquals(SyncEntityStatus.FAILED, failed.status());
        assertTrue(failed.message().contains("PROPERTY"));
    }

    @Test
    @DisplayName("deve isolar falha de um handler mantendo o processamento das demais entidades")
    void shouldIsolateHandlerFailure() {
        final var boom = entity(OfflineEntityType.PRODUCER);
        final var fine = entity(OfflineEntityType.PRODUCER);

        final var useCase = new UploadSyncUseCase(
                List.of(handler(OfflineEntityType.PRODUCER, Set.of(), cmd -> {
                    if (cmd.localId().equals(boom.localId())) {
                        throw new IllegalStateException("falha proposital");
                    }
                    return ok(cmd);
                })),
                idMappingGateway);

        final var results = useCase.execute(command(boom, fine));

        assertEquals(SyncEntityStatus.FAILED, resultOf(results, boom.localId()).status());
        assertEquals("falha proposital", resultOf(results, boom.localId()).message());
        assertEquals(SyncEntityStatus.CREATED, resultOf(results, fine.localId()).status());
    }

    @Test
    @DisplayName("deve lançar exceção quando batch excede 100 entidades")
    void shouldThrowWhenBatchExceeds100() {
        final var useCase = new UploadSyncUseCase(
                List.of(handler(OfflineEntityType.PRODUCER, Set.of(), cmd -> ok(cmd))),
                idMappingGateway);

        final var entities = new ArrayList<OfflineEntityCommand>();
        for (int i = 0; i <= 100; i++) {
            entities.add(entity(OfflineEntityType.PRODUCER));
        }
        final var command = new UploadSyncCommand(UserID.unique(), entities);

        assertThrows(DomainException.class, () -> useCase.execute(command));
    }

    @Test
    @DisplayName("deve falhar na inicialização quando há handler duplicado para o mesmo tipo")
    void shouldFailOnStartupForDuplicateHandler() {
        assertThrows(DomainException.class, () -> new UploadSyncUseCase(
                List.of(
                        handler(OfflineEntityType.PRODUCER, Set.of(), cmd -> ok(cmd)),
                        handler(OfflineEntityType.PRODUCER, Set.of(), cmd -> ok(cmd))
                ),
                idMappingGateway));
    }

    @Test
    @DisplayName("deve falhar na inicialização quando há dependência cíclica entre handlers")
    void shouldFailOnStartupForCyclicDependencies() {
        assertThrows(DomainException.class, () -> new UploadSyncUseCase(
                List.of(
                        handler(OfflineEntityType.PRODUCER, Set.of(OfflineEntityType.PROPERTY), cmd -> ok(cmd)),
                        handler(OfflineEntityType.PROPERTY, Set.of(OfflineEntityType.PRODUCER), cmd -> ok(cmd))
                ),
                idMappingGateway));
    }

    // --- helpers ---

    private static UploadSyncCommand command(final OfflineEntityCommand... entities) {
        return new UploadSyncCommand(UserID.unique(), List.of(entities));
    }

    private static OfflineEntityCommand entity(final OfflineEntityType type) {
        return new OfflineEntityCommand(type, UUID.randomUUID(), Map.of());
    }

    private static SyncEntityResult ok(final OfflineEntityCommand cmd) {
        return new SyncEntityResult(cmd.localId(), UUID.randomUUID(), SyncEntityStatus.CREATED, null);
    }

    private static SyncEntityResult resultOf(final List<SyncEntityResult> results, final UUID localId) {
        return results.stream().filter(r -> r.localId().equals(localId)).findFirst().orElseThrow();
    }

    private static SyncEntityHandler recordingHandler(final OfflineEntityType type,
                                                      final Set<OfflineEntityType> deps,
                                                      final List<OfflineEntityType> log) {
        return handler(type, deps, cmd -> {
            log.add(type);
            return ok(cmd);
        });
    }

    private static SyncEntityHandler handler(final OfflineEntityType type,
                                             final Set<OfflineEntityType> deps,
                                             final Function<OfflineEntityCommand, SyncEntityResult> behavior) {
        return new SyncEntityHandler() {
            @Override
            public OfflineEntityType type() {
                return type;
            }

            @Override
            public Set<OfflineEntityType> dependencies() {
                return deps;
            }

            @Override
            public SyncEntityResult handle(final OfflineEntityCommand command, final SyncContext context) {
                return behavior.apply(command);
            }
        };
    }
}
