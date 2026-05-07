package br.gov.pr.idr.legacy.entity.propertycollaborator;

import br.gov.pr.idr.legacy.entity.property.Property;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

@Entity @Audited
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertyCollaborator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @NotNull
    @ManyToOne
    private Property property;

    private String name;

    private String hoursPerDay;

}
