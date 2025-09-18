package br.edu.utfpr.ProjetoIDRAPI.entity.propertyequipimprove;

import br.edu.utfpr.ProjetoIDRAPI.entity.property.Property;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertyEquipImprove {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @ManyToOne
    private Property property;

    @NotNull
    private String type;

    @NotNull
    private String name;

    private Integer amount;

    private BigDecimal unitPrice;

    private BigDecimal percentDairyCattle;

    private Integer lifespan;

    private LocalDate acquisitionDate;

    private BigDecimal moneyDairyCattle;
}
