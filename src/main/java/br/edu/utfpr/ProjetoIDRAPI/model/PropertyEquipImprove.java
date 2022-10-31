package br.edu.utfpr.ProjetoIDRAPI.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertyEquipImprove {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Property propertyEquipImprove;

    @NotNull
    private String type;

    @NotNull
    private String name;

    @NotNull
    private Integer quantity;

    private BigDecimal unityValue;

    private BigDecimal percentageCattle;

    private Integer utilLife;

    private Date aquisitionDate;

    private BigDecimal valueCattle;
}
