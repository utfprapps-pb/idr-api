package br.gov.pr.idr.infra.shared.persistence;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SyncableJPAEntity — Base Entity")
class SyncableJPAEntityTest {

    private static class ConcreteSyncableEntity extends SyncableJPAEntity {
    }

    @Nested
    @DisplayName("isDeleted()")
    class IsDeleted {

        @Test
        @DisplayName("deve retornar falso quando deletedAt não foi definido")
        void shouldReturnFalseWhenDeletedAtIsNull() {
            final var entity = new ConcreteSyncableEntity();

            assertFalse(entity.isDeleted());
            assertNull(entity.getDeletedAt());
        }

        @Test
        @DisplayName("deve retornar verdadeiro quando a entidade foi marcada como deletada")
        void shouldReturnTrueWhenMarkedAsDeleted() {
            final var entity = new ConcreteSyncableEntity();

            entity.markDeleted();

            assertTrue(entity.isDeleted());
            assertNotNull(entity.getDeletedAt());
        }
    }

    @Nested
    @DisplayName("Versão")
    class Version {

        @Test
        @DisplayName("deve aplicar a versão informada")
        void shouldApplyVersion() {
            final var entity = new ConcreteSyncableEntity();

            entity.applyVersion(5L);

            assertEquals(5L, entity.getVersion());
        }
    }
}
