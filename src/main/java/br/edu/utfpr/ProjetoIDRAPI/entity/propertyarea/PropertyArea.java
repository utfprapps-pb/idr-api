package br.edu.utfpr.ProjetoIDRAPI.entity.propertyarea;

import br.edu.utfpr.ProjetoIDRAPI.entity.property.Property;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class PropertyArea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "property_id", referencedColumnName = "id")
    private Property property;

    private Double dairyCattleFarming;
    private Double perennialPasture;
    private Double summerPlowing;
    private Double winterPlowing;
}
