package br.gov.pr.idr.legacy.entity.animal;

import java.time.LocalDate;
import java.util.UUID;
import org.hibernate.envers.Audited;
import br.gov.pr.idr.legacy.entity.breed.Breed;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity @Audited
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @Column(name = "property_id", nullable = false)
    private UUID propertyId;

    @ManyToOne
    private Animal animalMother;

    @NotNull
    @ManyToOne
    private Breed breed;

    private String type;

    private String identifier;

    //Sexo do animal(M ou F)
    private String gender;

    //Condição de Nascimento(Vivo ou Morto)
    private String bornCondition;

    private LocalDate bornDate;

    private Float bornWeight;

    private Float previousWeight;

    private Float currentWeight;

    private Float ecc;
}
