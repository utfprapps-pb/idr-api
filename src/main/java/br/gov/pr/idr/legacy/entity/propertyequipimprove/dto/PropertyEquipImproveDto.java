package br.gov.pr.idr.legacy.entity.propertyequipimprove.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PropertyEquipImproveDto {

    private Long id;

    private String type;

    private String name;

    private Integer quantity;

    private BigDecimal unityValue;

    private BigDecimal percentageCattle;

    private Integer utilLife;

    private String aquisitionDate;

    private BigDecimal valueCattle;

}
