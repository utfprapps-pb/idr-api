package br.edu.utfpr.ProjetoIDRAPI.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.utfpr.ProjetoIDRAPI.model.Region;
import br.edu.utfpr.ProjetoIDRAPI.service.RegionService;

@RestController
@RequestMapping("Regions")
public class RegionController {
	private final RegionService regionService;
	private ModelMapper modelMapper;
	
	public RegionController(RegionService regionService, ModelMapper modelMapper) {
		this.regionService = regionService;
		this.modelMapper = modelMapper;
	}
	
	@GetMapping("/findId/{id}")
	public ResponseEntity<Region> findOneRegionById(@PathVariable Long id){
		return ResponseEntity.ok(regionService.findOneById(id));
	}
	
	@GetMapping("/findName/{name}")
	public ResponseEntity<Region> findOneRegionById(@PathVariable String name){
		return ResponseEntity.ok(regionService.findOneByNome(name));
	}
	
	@GetMapping("/listRegions")
	public ResponseEntity<List<Region>> listAllRegions(){
		return ResponseEntity.ok(regionService.findAll());
	}
}
