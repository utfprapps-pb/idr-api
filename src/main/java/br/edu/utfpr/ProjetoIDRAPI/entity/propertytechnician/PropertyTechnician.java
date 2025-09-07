package br.edu.utfpr.ProjetoIDRAPI.entity.propertytechnician;

import br.edu.utfpr.ProjetoIDRAPI.entity.user.User;
import br.edu.utfpr.ProjetoIDRAPI.entity.compositepropertytechnician.CompositePropertyTechnician;
import br.edu.utfpr.ProjetoIDRAPI.entity.property.Property;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    @NotNull
    @MapsId("property")
    @ManyToOne
    private Property property;
}
