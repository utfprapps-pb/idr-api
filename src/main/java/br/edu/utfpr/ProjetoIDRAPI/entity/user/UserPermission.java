package br.edu.utfpr.ProjetoIDRAPI.entity.user;

import br.edu.utfpr.ProjetoIDRAPI.entity.compositeuserpermission.CompositeUserPermission;
import br.edu.utfpr.ProjetoIDRAPI.entity.permission.Permission;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import org.hibernate.envers.Audited;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity @Audited
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPermission {

    @EmbeddedId
    private CompositeUserPermission id;

    @NotNull
    @MapsId("user")
    @ManyToOne
    private User user;

    @NotNull
    @MapsId("permission")
    @ManyToOne
    private Permission permission;
}
