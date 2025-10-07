package br.edu.utfpr.ProjetoIDRAPI.entity.animal;

import br.edu.utfpr.ProjetoIDRAPI.entity.breed.Breed;
import br.edu.utfpr.ProjetoIDRAPI.entity.property.Property;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

@Entity
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
