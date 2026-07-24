package br.gov.pr.idr.domain.property_management.sync.mapping;

import br.gov.pr.idr.domain.shared.tactical.Identifier;

import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.UUID;

public record SyncIdMappingID(UUID technicianId, UUID localId) implements Identifier {

    public SyncIdMappingID {
        Objects.requireNonNull(technicianId, "technicianId não pode ser nulo");
        Objects.requireNonNull(localId, "localId não pode ser nulo");
    }

    public static SyncIdMappingID from(final UUID technicianId, final UUID localId) {
        return new SyncIdMappingID(technicianId, localId);
    }

    @Override
    public UUID id() {
        final var buffer = ByteBuffer.allocate(32);
        buffer.putLong(technicianId.getMostSignificantBits());
        buffer.putLong(technicianId.getLeastSignificantBits());
        buffer.putLong(localId.getMostSignificantBits());
        buffer.putLong(localId.getLeastSignificantBits());
        return UUID.nameUUIDFromBytes(buffer.array());
    }
}
