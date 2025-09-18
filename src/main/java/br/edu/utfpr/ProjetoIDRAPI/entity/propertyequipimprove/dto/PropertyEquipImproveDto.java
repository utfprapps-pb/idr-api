package br.edu.utfpr.ProjetoIDRAPI.entity.propertyequipimprove.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PropertyEquipImproveDto {

    private long id;

    private String type;

    private String name;

    private Integer amount;

    private BigDecimal unitPrice;

    private BigDecimal percentDairyCattle;

    private Integer lifespan;

    private String acquisitionDate;

    private BigDecimal moneyDairyCattle;

}
