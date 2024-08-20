package br.edu.utfpr.ProjetoIDRAPI.entity.landproduct.dto;

import br.edu.utfpr.ProjetoIDRAPI.entity.property.dto.PropertyDto;
import lombok.Data;

@Data
public class LandProductDto {

    private long id;

    private PropertyDto property;

    private String useDate;

    private Integer quantity;

    private String usedFor;
}
