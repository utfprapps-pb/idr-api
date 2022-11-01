package br.edu.utfpr.ProjetoIDRAPI.controller;

import br.edu.utfpr.ProjetoIDRAPI.dto.PropertyEquipImproveDto;
import br.edu.utfpr.ProjetoIDRAPI.model.PropertyEquipImprove;
import br.edu.utfpr.ProjetoIDRAPI.service.PropertyEquipImproveService;
import br.edu.utfpr.ProjetoIDRAPI.utils.GenericResponse;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("propertyEquipImproves")
public class PropertyEquipImproveController {

    private final PropertyEquipImproveService service;
    private ModelMapper modelMapper;

    public PropertyEquipImproveController(PropertyEquipImproveService service, ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GenericResponse createRegister(@RequestBody @Valid PropertyEquipImprove propertyEquipImprove) {
        service.save(propertyEquipImprove);
        return new GenericResponse("Registro inserido com sucesso");
    }

    @PutMapping
    public GenericResponse updateRegister(@RequestBody @Valid PropertyEquipImprove propertyEquipImprove) {
        service.save(propertyEquipImprove);
        return new GenericResponse("Registro atualizado com sucesso");
    }

    @DeleteMapping("{id}")
    public GenericResponse delete(@PathVariable Long id) {
        service.delete(id);
        return new GenericResponse("Registro excluido com sucesso");
    }

    @GetMapping
    public ResponseEntity<List<PropertyEquipImproveDto>> findAll() {
        return ResponseEntity.ok(
                service.findAll()
                        .stream()
                        .map(this::convertEntityToDto)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("{id}")
    public ResponseEntity<PropertyEquipImproveDto> findOne(@PathVariable Long id) {
        return ResponseEntity.ok(convertEntityToDto(service.findOne(id)));
    }

    private PropertyEquipImproveDto convertEntityToDto(PropertyEquipImprove propertyEquipImprove) {
        return modelMapper.map(propertyEquipImprove, PropertyEquipImproveDto.class);
    }
}
