package br.edu.utfpr.ProjetoIDRAPI.controller;

import br.edu.utfpr.ProjetoIDRAPI.dto.PropertyCollaboratorDto;
import br.edu.utfpr.ProjetoIDRAPI.model.PropertyCollaborator;
import br.edu.utfpr.ProjetoIDRAPI.service.CrudService;
import br.edu.utfpr.ProjetoIDRAPI.service.PropertyCollaboratorService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("propertyCollaborators")
public class PropertyCollaboratorController extends CrudController<PropertyCollaborator, PropertyCollaboratorDto, Long> {

    private final PropertyCollaboratorService service;
    private ModelMapper modelMapper;

    public PropertyCollaboratorController(PropertyCollaboratorService service, ModelMapper modelMapper) {
        super(PropertyCollaborator.class, PropertyCollaboratorDto.class);
    	this.service = service;
        this.modelMapper = modelMapper;
    }
    
    @Override
    protected CrudService<PropertyCollaborator, Long> getService() {
    	return this.service;
    }

    @Override
    protected ModelMapper getModelMapper() {
    	return this.modelMapper;
    }
    
    @GetMapping("/collabProperty/{id}")
    public ResponseEntity<List<PropertyCollaboratorDto>> findAllByPropertyId(@PathVariable Long id) {
        return ResponseEntity.ok(
                service.findByPropertyId(id)
                        .stream()
                        .map(this::convertEntityToDto)
                        .collect(Collectors.toList())
        );
    }
    
    private PropertyCollaboratorDto convertEntityToDto(PropertyCollaborator propertyCollaborator) {
        return modelMapper.map(propertyCollaborator, PropertyCollaboratorDto.class);
    }
}
