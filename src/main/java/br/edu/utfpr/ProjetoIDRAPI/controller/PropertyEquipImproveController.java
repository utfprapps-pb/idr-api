package br.edu.utfpr.ProjetoIDRAPI.controller;

import br.edu.utfpr.ProjetoIDRAPI.dto.PropertyEquipImproveDto;
import br.edu.utfpr.ProjetoIDRAPI.model.PropertyEquipImprove;
import br.edu.utfpr.ProjetoIDRAPI.service.CrudService;
import br.edu.utfpr.ProjetoIDRAPI.service.PropertyEquipImproveService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("propertyEquipImproves")
public class PropertyEquipImproveController extends CrudController<PropertyEquipImprove, PropertyEquipImproveDto, Long>{

    private final PropertyEquipImproveService service;
    private ModelMapper modelMapper;

    public PropertyEquipImproveController(PropertyEquipImproveService service, ModelMapper modelMapper) {
        super(PropertyEquipImprove.class,PropertyEquipImproveDto.class);
    	this.service = service;
        this.modelMapper = modelMapper;
    }
    
	@Override
	protected CrudService<PropertyEquipImprove, Long> getService() {
		return this.service;
	}

	@Override
	protected ModelMapper getModelMapper() {
		return this.modelMapper;
	}
    
    @GetMapping("/propertyEquips/{id}")
    public ResponseEntity<List<PropertyEquipImproveDto>> findAllByPropertyId(@PathVariable Long id) {
        return ResponseEntity.ok(
                service.findByPropertyId(id)
                        .stream()
                        .map(this::convertEntityToDto)
                        .collect(Collectors.toList())
        );
    }

    private PropertyEquipImproveDto convertEntityToDto(PropertyEquipImprove propertyEquipImprove) {
        return modelMapper.map(propertyEquipImprove, PropertyEquipImproveDto.class);
    }
}
