package br.edu.utfpr.ProjetoIDRAPI.entity.propertyTechnician;

import br.edu.utfpr.ProjetoIDRAPI.entity.user.User;
import br.edu.utfpr.ProjetoIDRAPI.entity.compositePropertyTechnician.CompositePropertyTechnician;
import br.edu.utfpr.ProjetoIDRAPI.entity.property.Property;
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
