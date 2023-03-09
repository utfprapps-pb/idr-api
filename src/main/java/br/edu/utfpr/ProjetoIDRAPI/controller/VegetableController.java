package br.edu.utfpr.ProjetoIDRAPI.controller;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	
	@GetMapping("/findName/{name}")
	public ResponseEntity<VegetableDto> findByName(@PathVariable String name){
		Vegetable entity = service.findByName(name);
		
		if(entity != null) {
			return ResponseEntity.ok(convertToDto(service.findByName(name)));
		} else {
    		return ResponseEntity.noContent().build();
    	}
	}
	
	private VegetableDto convertToDto(Vegetable vegetable) {
		return modelMapper.map(vegetable, VegetableDto.class);
	}
}
