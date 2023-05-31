package br.edu.utfpr.ProjetoIDRAPI.controller;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.utfpr.ProjetoIDRAPI.dto.AnimalDiseasesDto;
import br.edu.utfpr.ProjetoIDRAPI.model.AnimalDiseases;
import br.edu.utfpr.ProjetoIDRAPI.service.AnimalDiseasesService;
import br.edu.utfpr.ProjetoIDRAPI.service.CrudService;

@RestController
@RequestMapping("animalDiseases")
public class AnimalDiseasesController extends CrudController<AnimalDiseases, AnimalDiseasesDto, Long> {
	private final AnimalDiseasesService animalDiseasesService;
	private ModelMapper modelMapper;
	
	public AnimalDiseasesController(AnimalDiseasesService animalDiseasesService, ModelMapper modelMapper) {
		super(AnimalDiseases.class, AnimalDiseasesDto.class);
		this.animalDiseasesService = animalDiseasesService;
		this.modelMapper = modelMapper;
	}

	@Override
	protected CrudService<AnimalDiseases, Long> getService() {
		return this.animalDiseasesService;
	}

	@Override
	protected ModelMapper getModelMapper() {
		return this.modelMapper;
	}
	
}
