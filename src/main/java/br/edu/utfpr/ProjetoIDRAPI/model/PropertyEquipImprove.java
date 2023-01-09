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

    @JoinColumn(name = "Property_id")
    @ManyToOne
    private Property property;

    @NotNull
    private String type;

    @NotNull
    private String name;

    private Integer quantity;

    private BigDecimal unityValue;

    private BigDecimal percentageCattle;

    private Integer utilLife;

    private Date aquisitionDate;

    private BigDecimal valueCattle;
}
