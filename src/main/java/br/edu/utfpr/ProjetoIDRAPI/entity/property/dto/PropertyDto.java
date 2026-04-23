package br.edu.utfpr.ProjetoIDRAPI.entity.property.dto;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import br.edu.utfpr.ProjetoIDRAPI.entity.city.dto.CityDto;
import br.edu.utfpr.ProjetoIDRAPI.entity.propertyarea.PropertyArea;
import br.edu.utfpr.ProjetoIDRAPI.entity.propertyattachment.PropertyAttachmentDTO;
import br.edu.utfpr.ProjetoIDRAPI.entity.propertycollaborator.PropertyCollaborator;
import br.edu.utfpr.ProjetoIDRAPI.entity.propertytechnician.PropertyTechnician;
import br.edu.utfpr.ProjetoIDRAPI.entity.user.dto.UserDto;
import lombok.Data;

@Data
public class PropertyDto {
	
	private Long id;

    private String name;

    private UserDto producer;

    private CityDto city;
    
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