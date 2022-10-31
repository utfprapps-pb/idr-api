package br.edu.utfpr.ProjetoIDRAPI.dto;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

@Data
public class PropertyDto {
	
	private Long id;
	
    private String ocupationArea;
    
    private BigDecimal totalArea;
    
    private Byte[] soilMap;
    
    private BigInteger latitude;
    
    private BigInteger longitude;
    
    private Boolean leased;
}
