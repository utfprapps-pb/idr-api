package br.gov.pr.idr.legacy.entity.product;

import br.gov.pr.idr.legacy.entity.productcategory.ProductCategory;
import br.gov.pr.idr.legacy.entity.activeprinciple.ActivePrinciple;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

// Mantida apenas para Medication (legacy) compilar — ver Plano D de migração; sem controller/service próprio.
@Entity @Audited
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
