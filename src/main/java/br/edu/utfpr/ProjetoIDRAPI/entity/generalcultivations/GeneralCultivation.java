package br.edu.utfpr.ProjetoIDRAPI.entity.generalcultivations;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

@Entity
@Audited
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "general_cultivation")
public class GeneralCultivation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type;
    private Double dryMatter;
    private Double crudeProtein;
    private Double totalDigestibleNutrients;
    private Double calcium;
    private Double phosphorus;
    private Double nonFibrousCarbohydrates;
    private Double etherExtract;
    private Double rumenDegradableProtein;
}
