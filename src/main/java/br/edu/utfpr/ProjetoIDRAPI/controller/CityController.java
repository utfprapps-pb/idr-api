package br.edu.utfpr.ProjetoIDRAPI.controller;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.utfpr.ProjetoIDRAPI.model.City;
import br.edu.utfpr.ProjetoIDRAPI.service.CityService;
import br.edu.utfpr.ProjetoIDRAPI.service.CrudService;

@RestController
@RequestMapping("cities")
public class CityController extends CrudController<City, City, Long>{
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

	@GetMapping("/findName/{name}")
	public ResponseEntity<City> findOneByName(@PathVariable String name){
		return ResponseEntity.ok(cityService.findOneByNome(name));
	}
}