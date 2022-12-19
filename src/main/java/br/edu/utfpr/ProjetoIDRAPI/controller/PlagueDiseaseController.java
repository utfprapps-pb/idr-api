package br.edu.utfpr.ProjetoIDRAPI.controller;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.utfpr.ProjetoIDRAPI.dto.PlagueDiseaseDto;
import br.edu.utfpr.ProjetoIDRAPI.model.PlagueDisease;
import br.edu.utfpr.ProjetoIDRAPI.service.CrudService;
import br.edu.utfpr.ProjetoIDRAPI.service.PlagueDiseaseService;

@RestController
@RequestMapping("plaguesDiseases")
public class PlagueDiseaseController extends CrudController<PlagueDisease, PlagueDiseaseDto, Long>{
	private final PlagueDiseaseService service;
	private ModelMapper modelMapper;
	
	public PlagueDiseaseController(PlagueDiseaseService service, ModelMapper modelMapper) {
		super(PlagueDisease.class,PlagueDiseaseDto.class);
		this.service = service;
		this.modelMapper = modelMapper;
	}

	@Override
	protected CrudService<PlagueDisease, Long> getService() {
		return this.service;
	}

	@Override
	protected ModelMapper getModelMapper() {
		return this.modelMapper;
	}
}
