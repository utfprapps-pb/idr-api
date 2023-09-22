package br.edu.utfpr.ProjetoIDRAPI.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
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
