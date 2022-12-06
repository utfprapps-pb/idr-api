package br.edu.utfpr.ProjetoIDRAPI.controller;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.edu.utfpr.ProjetoIDRAPI.model.City;
import br.edu.utfpr.ProjetoIDRAPI.service.CityService;
import br.edu.utfpr.ProjetoIDRAPI.service.CrudService;

import javax.validation.Valid;

@RestController
@RequestMapping("cities")
public class CityController extends CrudController<City, City, Long>{
//Como cidade não tem um dto definido, quando chamado o extends do crud 
//foi passado para ele duas cidade no lugar de uma city e um dto
	private final CityService cityService;
	private ModelMapper modelMapper;

	public CityController(CityService cityService, ModelMapper modelMapper) {
		super(City.class, City.class);
		this.cityService = cityService;
		this.modelMapper = modelMapper;
	}

	@Override
	protected CrudService<City, Long> getService() {
		return this.cityService;
	}

	@Override
	protected ModelMapper getModelMapper() {
		return this.modelMapper;
	}

	@GetMapping("/findName")
	public ResponseEntity<City> findByName(@RequestBody @Valid String name){
		return ResponseEntity.ok(cityService.findByName(name));
	}
}