package br.gov.pr.idr.infra.iam.permission.persistence;

import br.gov.pr.idr.domain.iam.permission.Permission;
import br.gov.pr.idr.domain.iam.permission.PermissionID;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.springframework.security.core.GrantedAuthority;

@Entity(name = "Permission")
@AllArgsConstructor
@NoArgsConstructor
@Audited
@Getter
@Setter
public class PermissionJPAEntity implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @Override
    public String getAuthority() {
        return name;
    }

    public static PermissionJPAEntity from(final Permission permission) {
        return new PermissionJPAEntity(permission.getId().id(), permission.getName());
    }

    public Permission toDomain() {
        return Permission.with(PermissionID.from(id), name);
    }
}