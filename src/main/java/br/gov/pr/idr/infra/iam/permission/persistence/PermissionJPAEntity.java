package br.gov.pr.idr.infra.iam.permission.persistence;

import br.gov.pr.idr.domain.iam.permission.Permission;
import br.gov.pr.idr.domain.iam.permission.PermissionID;
import br.gov.pr.idr.domain.iam.user.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.springframework.security.core.GrantedAuthority;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity(name = "Permission")
@AllArgsConstructor
@NoArgsConstructor
@Audited
@Getter
@Setter
@Table(name = "users_permission")
public class PermissionJPAEntity implements GrantedAuthority {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Column(name = "read_only")
    private boolean readOnly = false;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "permission_regions", joinColumns = @JoinColumn(name = "permission_id"))
    @Column(name = "region_id")
    private Set<UUID> regionIds = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "permission_cities", joinColumns = @JoinColumn(name = "permission_id"))
    @Column(name = "city_id")
    private Set<UUID> cityIds = new HashSet<>();

    @Override
    public String getAuthority() {
        return role != null ? role.name() : null;
    }

    public static PermissionJPAEntity from(final Permission permission) {
        return new PermissionJPAEntity(
                permission.getId().id(),
                permission.getRole(),
                permission.isReadOnly(),
                permission.getRegionIds() != null ? new HashSet<>(permission.getRegionIds()) : new HashSet<>(),
                permission.getCityIds() != null ? new HashSet<>(permission.getCityIds()) : new HashSet<>()
        );
    }

    public Permission toDomain() {
        return Permission.with(
                PermissionID.from(id),
                role,
                readOnly,
                regionIds != null ? new HashSet<>(regionIds) : new HashSet<>(),
                cityIds != null ? new HashSet<>(cityIds) : new HashSet<>()
        );
    }
}
