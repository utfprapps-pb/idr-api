package br.edu.utfpr.ProjetoIDRAPI.entity.product;

import br.edu.utfpr.ProjetoIDRAPI.entity.productCategory.ProductCategory;
import br.edu.utfpr.ProjetoIDRAPI.entity.activePrinciple.ActivePrinciple;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private String applicationWay;

    @ManyToOne
    private ActivePrinciple activePrinciple;

    @ManyToOne
    private ProductCategory category;

}
