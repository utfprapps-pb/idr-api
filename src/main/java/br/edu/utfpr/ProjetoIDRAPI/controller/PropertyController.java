package br.edu.utfpr.ProjetoIDRAPI.controller;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.utfpr.ProjetoIDRAPI.dto.PropertyDto;
import br.edu.utfpr.ProjetoIDRAPI.model.Property;
import br.edu.utfpr.ProjetoIDRAPI.service.CrudService;
import br.edu.utfpr.ProjetoIDRAPI.service.PropertyService;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("properties")
public class PropertyController extends CrudController<Property, PropertyDto, Long> {
	private final PropertyService propertyService;
	private ModelMapper modelMapper;
	
	public PropertyController(PropertyService propertyService, ModelMapper modelMapper) {
		super(Property.class, PropertyDto.class);
		this.propertyService = propertyService;
		this.modelMapper = modelMapper;
	}
	
	@Override
	protected CrudService<Property, Long> getService() {
		return this.propertyService;
	}

	@Override
	protected ModelMapper getModelMapper() {
		return this.modelMapper;
	}
	
	@GetMapping("/userProperty/{id}")
	public ResponseEntity<List<PropertyDto>> findByUserId(@PathVariable Long id) {
		return ResponseEntity.ok(
				propertyService.findByUserId(id)
						.stream()
						.map(this::convertToDto)
						.collect(Collectors.toList())
		);
	}
	
	private PropertyDto convertToDto(Property property) {
    	return modelMapper.map(property, PropertyDto.class);
    }
}
