package br.gov.pr.idr.legacy.entity.property.dto;

import br.gov.pr.idr.legacy.entity.propertyarea.PropertyArea;
import br.gov.pr.idr.legacy.entity.propertyattachment.PropertyAttachmentDTO;
import br.gov.pr.idr.legacy.entity.propertycollaborator.PropertyCollaborator;
import br.gov.pr.idr.legacy.entity.propertytechnician.PropertyTechnician;
import lombok.Data;

import java.math.BigInteger;
import java.util.List;

@Data
public class PropertyDto {

    private Long id;

    private String name;

//    private UserDto producer;

    private TotalAreaDto totalArea;

    private BigInteger latitude;

    private BigInteger longitude;

    private Boolean leased;

    private Double nakedAveragePrice;

    private Double leaseAveragePrice;

    private PropertyArea area;

    private PropertyAttachmentDTO attachment;

    private List<PropertyCollaborator> collaborators;

    private List<PropertyTechnician> technicians;

}