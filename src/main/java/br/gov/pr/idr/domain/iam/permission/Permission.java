package br.gov.pr.idr.domain.iam.permission;

import br.gov.pr.idr.domain.iam.user.UserRole;
import br.gov.pr.idr.domain.shared.Entity;
import br.gov.pr.idr.domain.shared.validation.DomainError;
import br.gov.pr.idr.domain.shared.validation.ValidationHandler;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Permission extends Entity<PermissionID> {

    private final UserRole role;
    private final boolean readOnly;
    private final Set<UUID> regionIds;
    private final Set<UUID> cityIds;

    Permission(final PermissionID id, final UserRole role, final boolean readOnly,
               final Set<UUID> regionIds, final Set<UUID> cityIds) {
        super(id);
        this.role = role;
        this.readOnly = readOnly;
        this.regionIds = regionIds != null ? regionIds : new HashSet<>();
        this.cityIds = cityIds != null ? cityIds : new HashSet<>();
        super.selfValidate();
    }

    public static Permission create(final UserRole role, final boolean readOnly,
                                    final Set<UUID> regionIds, final Set<UUID> cityIds) {
        return new Permission(PermissionID.unique(), role, readOnly, regionIds, cityIds);
    }

    public static Permission with(final PermissionID id, final UserRole role, final boolean readOnly,
                                  final Set<UUID> regionIds, final Set<UUID> cityIds) {
        return new Permission(id, role, readOnly, regionIds, cityIds);
    }

    @Override
    public void validate(ValidationHandler handler) {
        if (this.role == null) {
            handler.append(DomainError.from("role", "Role da permissão não pode ser nula."));
        }
    }

    public UserRole getRole() {
        return role;
    }

    public String getName() {
        return role != null ? role.name() : null;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public Set<UUID> getRegionIds() {
        return regionIds;
    }

    public Set<UUID> getCityIds() {
        return cityIds;
    }
}
