package br.edu.utfpr.ProjetoIDRAPI.controller;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.utfpr.ProjetoIDRAPI.dto.VegetableDto;
import br.edu.utfpr.ProjetoIDRAPI.model.Vegetable;
import br.edu.utfpr.ProjetoIDRAPI.service.CrudService;
import br.edu.utfpr.ProjetoIDRAPI.service.VegetableService;

@RestController
@RequestMapping("vegetables")
public class VegetableController extends CrudController<Vegetable, VegetableDto, Long>{
	private final VegetableService service;
	private ModelMapper modelMapper;
	
	public VegetableController(VegetableService service, ModelMapper modelMapper) {
		super(Vegetable.class,VegetableDto.class);
		this.service = service;
		this.modelMapper = modelMapper;
	}
	
	@Override
	protected CrudService<Vegetable, Long> getService() {
		return this.service;
	}

	@Override
	protected ModelMapper getModelMapper() {
		return this.modelMapper;
	}
	
	@GetMapping("/findName")
	public ResponseEntity<VegetableDto> findByName(@RequestHeader @Valid String name){
		return ResponseEntity.ok(convertToDto(service.findByName(name)));
	}
	
	private VegetableDto convertToDto(Vegetable vegetable) {
		return modelMapper.map(vegetable, VegetableDto.class);
	}
}
