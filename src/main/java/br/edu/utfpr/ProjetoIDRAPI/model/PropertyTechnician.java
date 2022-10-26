package br.edu.utfpr.ProjetoIDRAPI.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertyTechnician {

    @EmbeddedId
    private CompositePropertyTechnician id;

    @MapsId("user")
    @ManyToOne
    private User user;

    @MapsId("property")
    @ManyToOne
    private Property property;
}
