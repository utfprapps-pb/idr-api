package br.edu.utfpr.ProjetoIDRAPI.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.utfpr.ProjetoIDRAPI.model.City;
import br.edu.utfpr.ProjetoIDRAPI.service.CityService;

@RestController
@RequestMapping("cities")
public class CityController {
	private final CityService cityService;
	private ModelMapper modelMapper;
	
	public CityController(CityService cityService, ModelMapper modelMapper) {
		this.cityService = cityService;
		this.modelMapper = modelMapper;
	}
	
	@GetMapping("{id}")
	public ResponseEntity<City> findOne(@PathVariable Long id){
		return ResponseEntity.ok(cityService.findOneById(id));
	}
	
	@GetMapping("/findName/{name}")
	public ResponseEntity<City> findOneByName(@PathVariable String name){
		return ResponseEntity.ok(cityService.findOneByNome(name));
	}
	
	@GetMapping
	public ResponseEntity<List<City>> listAll(){
		return ResponseEntity.ok(cityService.findAll());
	}
}
