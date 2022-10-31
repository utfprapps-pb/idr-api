package br.edu.utfpr.ProjetoIDRAPI.controller;

import br.edu.utfpr.ProjetoIDRAPI.model.PropertyEquipImprove;
import br.edu.utfpr.ProjetoIDRAPI.service.PropertyEquipImproveService;
import br.edu.utfpr.ProjetoIDRAPI.utils.GenericResponse;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

    @DeleteMapping("{id}")
    public GenericResponse delete(@PathVariable Long id) {
        service.delete(id);
        return new GenericResponse("Registro excluido com sucesso");
    }

    @GetMapping
    public ResponseEntity<List<PropertyEquipImprove>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<PropertyEquipImprove> findOne(@PathVariable Long id) {
        return ResponseEntity.ok(service.findOne(id));
    }
}
