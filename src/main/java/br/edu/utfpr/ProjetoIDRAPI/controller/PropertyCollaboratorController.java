package br.edu.utfpr.ProjetoIDRAPI.controller;

import br.edu.utfpr.ProjetoIDRAPI.dto.PropertyCollaboratorDto;
import br.edu.utfpr.ProjetoIDRAPI.model.PropertyCollaborator;
import br.edu.utfpr.ProjetoIDRAPI.service.PropertyCollaboratorService;
import br.edu.utfpr.ProjetoIDRAPI.utils.GenericResponse;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("propertyCollaborators")
public class PropertyCollaboratorController {

    private final PropertyCollaboratorService service;
    private ModelMapper modelMapper;

    public PropertyCollaboratorController(PropertyCollaboratorService service, ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GenericResponse createRegister(@RequestBody @Valid PropertyCollaborator propertyCollaborator) {
        service.save(propertyCollaborator);
        return new GenericResponse("Registro inserido com sucesso");
    }

    @PutMapping
    public GenericResponse updateRegister(@RequestBody @Valid PropertyCollaborator propertyCollaborator) {
        service.save(propertyCollaborator);
        return new GenericResponse("Registro atualizado com sucesso");
    }

    @DeleteMapping("{id}")
    public GenericResponse deleteRegister(@PathVariable Long id) {
        service.delete(id);
        return new GenericResponse("Registro excluido com sucesso");
    }

    @GetMapping
    public ResponseEntity<List<PropertyCollaboratorDto>> findAll() {
        return ResponseEntity.ok(
                service.findAll()
                        .stream()
                        .map(this::convertEntityToDto)
                        .collect(Collectors.toList())
        );
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


    @GetMapping("{id}")
    public ResponseEntity<PropertyCollaboratorDto> findOne(@PathVariable Long id) {
        return ResponseEntity.ok(convertEntityToDto(service.findOne(id)));
    }

    private PropertyCollaboratorDto convertEntityToDto(PropertyCollaborator propertyCollaborator) {
        return modelMapper.map(propertyCollaborator, PropertyCollaboratorDto.class);
    }
}
