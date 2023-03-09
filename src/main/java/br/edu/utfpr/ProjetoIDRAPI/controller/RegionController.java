package br.edu.utfpr.ProjetoIDRAPI.controller;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.edu.utfpr.ProjetoIDRAPI.model.Region;
import br.edu.utfpr.ProjetoIDRAPI.service.CrudService;
import br.edu.utfpr.ProjetoIDRAPI.service.RegionService;

@RestController
@RequestMapping("regions")
public class RegionController extends CrudController<Region, Region, Long> {

	private final RegionService regionService;
	private ModelMapper modelMapper;
	
	public RegionController(RegionService regionService, ModelMapper modelMapper) {
		super(Region.class, Region.class);
		this.regionService = regionService;
		this.modelMapper = modelMapper;
	}

	@Override
	protected CrudService<Region, Long> getService() {
		return this.regionService;
	}

	@Override
	protected ModelMapper getModelMapper() {
		return this.modelMapper;
	}
	
	@GetMapping("/findName/{name}")
	public ResponseEntity<Region> findByName(@PathVariable String name){
		return ResponseEntity.ok(regionService.findByName(name));
	}
}
