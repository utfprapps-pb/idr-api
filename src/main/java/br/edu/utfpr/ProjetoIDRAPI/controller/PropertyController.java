package br.edu.utfpr.ProjetoIDRAPI.controller;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.edu.utfpr.ProjetoIDRAPI.dto.PropertyDto;
import br.edu.utfpr.ProjetoIDRAPI.model.Property;
import br.edu.utfpr.ProjetoIDRAPI.service.PropertyService;
import br.edu.utfpr.ProjetoIDRAPI.utils.GenericResponse;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("properties")
public class PropertyController {
	private final PropertyService propertyService;
	private ModelMapper modelMapper;
	
	public PropertyController(PropertyService propertyService, ModelMapper modelMapper) {
		this.propertyService = propertyService;
		this.modelMapper = modelMapper;
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public GenericResponse createProperty(@RequestBody @Valid Property property) {
		propertyService.save(property);
		return new GenericResponse("Propriedade criada com sucesso!");
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<PropertyDto> findOneProperty(@PathVariable Long id){
		Property property = propertyService.findOne(id);
		
		if(property != null) {
			return ResponseEntity.ok(convertToDto(propertyService.findOne(id)));
		} else {
			return ResponseEntity.noContent().build();
		}
	}
	
	@GetMapping("/listProperties")
	public ResponseEntity<List<PropertyDto>> listAllProperties(){
		return ResponseEntity.ok(propertyService.findAll().stream()
				.map(this::convertToDto)
				.collect(Collectors.toList()));
	}
	
	@PutMapping("/{id}")
	public GenericResponse updateAProperty(@RequestBody @Valid Property property) {
		propertyService.save(property);
		return new GenericResponse("Propriedade foi atualizado!");
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Property> removeAProperty(@PathVariable Long id){
		propertyService.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	private PropertyDto convertToDto(Property property) {
    	return modelMapper.map(property, PropertyDto.class);
    }
}
