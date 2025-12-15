package br.edu.utfpr.ProjetoIDRAPI.entity.perennialanualforage;

import br.edu.utfpr.ProjetoIDRAPI.entity.property.Property;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import org.hibernate.envers.Audited;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

@Entity @Audited
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerennialAnualForage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @ManyToOne
    private Property property;

    private String type;

    private Double price;

    private String forage;

    private Double area;

    private Double averageCost;

    private Integer utilLife;

    private LocalDate formationDate;

    private String note;

}
