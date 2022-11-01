package br.edu.utfpr.ProjetoIDRAPI.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class PropertyEquipImproveDto {

    private Long id;

    private String type;

    private String name;

    private Integer quantity;

    private BigDecimal unityValue;

    private BigDecimal percentageCattle;

    private Integer utilLife;

    private Date aquisitionDate;

    private BigDecimal valueCattle;

}
