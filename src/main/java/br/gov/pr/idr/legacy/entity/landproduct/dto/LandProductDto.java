package br.gov.pr.idr.legacy.entity.landproduct.dto;

import br.gov.pr.idr.legacy.entity.property.dto.PropertyDto;
import lombok.Data;

@Data
public class LandProductDto {

    private long id;

    private PropertyDto property;

    private String useDate;

    private Integer quantity;

    private String usedFor;
}
