package br.edu.utfpr.ProjetoIDRAPI.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.validation.constraints.NotNull;

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
