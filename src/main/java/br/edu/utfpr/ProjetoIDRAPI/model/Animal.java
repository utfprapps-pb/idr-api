package br.edu.utfpr.ProjetoIDRAPI.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "Property_id")
    @ManyToOne
    private Property property;

    private String type;

    private String identifier;

    private String name;

    private String breed;

    private LocalDate bornDate;

    private Float bornWeight;

    private Float previousWeight;

    private Float currentWeight;

    private Float ecc;
}
