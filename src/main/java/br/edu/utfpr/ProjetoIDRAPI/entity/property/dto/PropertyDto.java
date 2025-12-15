package br.edu.utfpr.ProjetoIDRAPI.entity.property.dto;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import br.edu.utfpr.ProjetoIDRAPI.entity.propertyarea.PropertyArea;
import br.edu.utfpr.ProjetoIDRAPI.entity.propertyattachment.PropertyAttachmentDTO;
import br.edu.utfpr.ProjetoIDRAPI.entity.propertycollaborator.PropertyCollaborator;
import br.edu.utfpr.ProjetoIDRAPI.entity.propertytechnician.PropertyTechnician;
import lombok.Data;

@Data
public class PropertyDto {
	
	private long id;
	
    private String ocupationArea;
    
    private BigDecimal totalArea;

    private BigInteger latitude;
    
    private BigInteger longitude;
    
    private Boolean leased;

    private String name;
    private String city;
    private String state;
    private Double nakedAveragePrice;
    private Double leaseAveragePrice;
    private String farmer;
    private List<PropertyCollaborator> collaborators;
    private PropertyArea area;
    private List<PropertyTechnician> technicians;
    private PropertyAttachmentDTO attachment;
}