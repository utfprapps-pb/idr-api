package br.edu.utfpr.ProjetoIDRAPI.entity.animal;

import br.edu.utfpr.ProjetoIDRAPI.entity.breed.Breed;
import br.edu.utfpr.ProjetoIDRAPI.entity.property.Property;
import br.edu.utfpr.ProjetoIDRAPI.enums.Sex;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import org.hibernate.envers.Audited;
import jakarta.validation.constraints.NotNull;

@Entity @Audited
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @ManyToOne
    private Property property;

    @ManyToOne
    private Animal animalMother;

    @NotNull
    @ManyToOne
    private Breed breed;

    private String type;

    private String identifier;

    @Enumerated(EnumType.STRING)
    @Column(length = 1, nullable = false)
    private Sex sex;

    //Condição de Nascimento(Vivo ou Morto)
    private String bornCondition;

    private LocalDate bornDate;

    private Float bornWeight;

    private Float previousWeight;

    private Float currentWeight;

    private Float ecc;
}
