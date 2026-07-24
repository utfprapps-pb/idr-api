package br.gov.pr.idr.domain.property_management.sync;

import br.gov.pr.idr.domain.iam.user.UserID;
import br.gov.pr.idr.domain.property_management.sync.mapping.SyncIdMapping;
import br.gov.pr.idr.domain.property_management.sync.vo.OfflineEntityType;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotificationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SyncIdMapping — Value Object")
class SyncIdMappingTest {

    private static final UserID TECHNICIAN_ID = UserID.unique();
    private static final UUID LOCAL_ID = UUID.randomUUID();
    private static final UUID SERVER_ID = UUID.randomUUID();
    private static final OfflineEntityType ENTITY_TYPE = OfflineEntityType.PROPERTY;

    @Nested
    @DisplayName("Criação via factory create()")
    class Create {

        @Test
        @DisplayName("deve criar mapeamento sem data de sincronização")
        void shouldCreateWithoutSyncedAt() {
            final var mapping = SyncIdMapping.create(TECHNICIAN_ID, LOCAL_ID, SERVER_ID, ENTITY_TYPE);

            assertEquals(TECHNICIAN_ID, mapping.technicianId());
            assertEquals(LOCAL_ID, mapping.localId());
            assertEquals(SERVER_ID, mapping.serverId());
            assertEquals(ENTITY_TYPE, mapping.entityType());
            assertNull(mapping.syncedAt());
        }
    }

    @Nested
    @DisplayName("Criação via factory from()")
    class From {

        @Test
        @DisplayName("deve criar mapeamento com todos os campos, incluindo data de sincronização")
        void shouldCreateFromAllFields() {
            final var syncedAt = Instant.now();

            final var mapping = SyncIdMapping.from(TECHNICIAN_ID, LOCAL_ID, SERVER_ID, ENTITY_TYPE, syncedAt);

            assertEquals(TECHNICIAN_ID, mapping.technicianId());
            assertEquals(LOCAL_ID, mapping.localId());
            assertEquals(SERVER_ID, mapping.serverId());
            assertEquals(ENTITY_TYPE, mapping.entityType());
            assertEquals(syncedAt, mapping.syncedAt());
        }
    }

    @Nested
    @DisplayName("Validação do construtor compacto")
    class ConstructorValidation {

        @Test
        @DisplayName("deve rejeitar mapeamento com technicianId nulo")
        void shouldRejectNullTechnicianId() {
            assertThrows(NullPointerException.class,
                    () -> SyncIdMapping.from(null, LOCAL_ID, SERVER_ID, ENTITY_TYPE, Instant.now()));
        }

        @Test
        @DisplayName("deve rejeitar mapeamento com localId nulo")
        void shouldRejectNullLocalId() {
            assertThrows(NullPointerException.class,
                    () -> SyncIdMapping.from(TECHNICIAN_ID, null, SERVER_ID, ENTITY_TYPE, Instant.now()));
        }

        @Test
        @DisplayName("deve rejeitar mapeamento com serverId nulo")
        void shouldRejectNullServerId() {
            assertThrows(NotificationException.class,
                    () -> SyncIdMapping.from(TECHNICIAN_ID, LOCAL_ID, null, ENTITY_TYPE, Instant.now()));
        }

        @Test
        @DisplayName("deve rejeitar mapeamento com entityType nulo")
        void shouldRejectNullEntityType() {
            assertThrows(NotificationException.class,
                    () -> SyncIdMapping.from(TECHNICIAN_ID, LOCAL_ID, SERVER_ID, null, Instant.now()));
        }
    }
}
