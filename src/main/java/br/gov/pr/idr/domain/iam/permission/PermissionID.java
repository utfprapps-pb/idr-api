package br.gov.pr.idr.domain.iam.permission;

import br.gov.pr.idr.domain.shared.Identifier;

import java.util.UUID;

public record PermissionID(UUID id) implements Identifier {

    public static PermissionID from(final UUID id) {
        return new PermissionID(id);
    }

    public static PermissionID unique() {
        return from(UUID.randomUUID());
    }
}
