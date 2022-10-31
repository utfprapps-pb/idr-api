package br.edu.utfpr.ProjetoIDRAPI.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertyTechnician {

    @EmbeddedId
    private CompositePropertyTechnician id;

    @NotNull
    @MapsId("user")
    @ManyToOne
    private User user;

    @NotNull
    @MapsId("property")
    @ManyToOne
    private Property property;
}
