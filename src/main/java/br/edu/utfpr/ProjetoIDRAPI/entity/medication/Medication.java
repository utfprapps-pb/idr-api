package br.edu.utfpr.ProjetoIDRAPI.entity.medication;

import br.edu.utfpr.ProjetoIDRAPI.entity.product.Product;
import br.edu.utfpr.ProjetoIDRAPI.entity.animal.Animal;
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
public class Medication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private LocalDate applicationDate;

    @NotNull
    private String appliedDose;

    @NotNull
    private String applicationWay;

    @ManyToOne
    private Product product;

    @ManyToOne
    private Animal animal;

}
