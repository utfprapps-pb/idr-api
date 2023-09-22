package br.edu.utfpr.ProjetoIDRAPI.controller;

import br.edu.utfpr.ProjetoIDRAPI.utils.GenericResponse;
import jakarta.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import br.edu.utfpr.ProjetoIDRAPI.dto.AnimalDiseasesDto;
import br.edu.utfpr.ProjetoIDRAPI.model.AnimalDiseases;
import br.edu.utfpr.ProjetoIDRAPI.service.AnimalDiseasesService;
import br.edu.utfpr.ProjetoIDRAPI.service.CrudService;

import java.util.List;

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

	@PostMapping("/sendAnimalDiseases")
	@ResponseStatus(HttpStatus.CREATED)
	public GenericResponse createRegister(@RequestBody @Valid List<AnimalDiseases> animalDiseases) {
		animalDiseasesService.saveListAnimalDiseases(animalDiseases);
		return new GenericResponse("Registros inseridos com sucesso");
	}
}
