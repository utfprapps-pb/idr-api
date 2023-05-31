package br.edu.utfpr.ProjetoIDRAPI.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PropertyEquipImproveDto {

    private long id;

    private String type;

    private String name;

    private Integer quantity;

    private BigDecimal unityValue;

    private BigDecimal percentageCattle;

    private Integer utilLife;

    private String aquisitionDate;

    private BigDecimal valueCattle;

}
