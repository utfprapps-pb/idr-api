package br.edu.utfpr.ProjetoIDRAPI.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

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

    @NotNull
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
