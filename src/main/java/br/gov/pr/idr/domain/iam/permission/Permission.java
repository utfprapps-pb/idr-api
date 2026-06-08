package br.gov.pr.idr.domain.iam.permission;

import br.gov.pr.idr.domain.shared.Entity;
import br.gov.pr.idr.domain.shared.validation.DomainError;
import br.gov.pr.idr.domain.shared.validation.ValidationHandler;

public class Permission extends Entity<PermissionID> {

    private final String name;

    Permission(final PermissionID id, final String name) {
        super(id);
        this.name = name;
        super.selfValidate();
    }

    public static Permission create(final String name) {
        return new Permission(PermissionID.unique(), name);
    }

    public static Permission with(final PermissionID id, final String name) {
        return new Permission(id, name);
    }

    @Override
    public void validate(ValidationHandler handler) {
        if (this.name == null || this.name.isBlank()) {
            handler.append(DomainError.from("name", "Nome da permissão não pode ser nulo ou vazio."));
        }
    }

    public String getName() {
        return name;
    }
}
