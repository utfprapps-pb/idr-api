package br.edu.utfpr.ProjetoIDRAPI.entity.propertycollaborator;

import br.edu.utfpr.ProjetoIDRAPI.entity.property.Property;
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
    private long id;

    @JsonIgnore
    @NotNull
    @ManyToOne
    private Property property;

    @NotNull
    private String collaboratorName;

    @NotNull
    private Integer workHours;

    @NotNull
    private Integer workDays;

}
