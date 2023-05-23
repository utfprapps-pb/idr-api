package br.edu.utfpr.ProjetoIDRAPI.controller;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.utfpr.ProjetoIDRAPI.dto.VegetableDiseaseDto;
import br.edu.utfpr.ProjetoIDRAPI.model.VegetableDisease;
import br.edu.utfpr.ProjetoIDRAPI.service.CrudService;
import br.edu.utfpr.ProjetoIDRAPI.service.VegetableDiseaseService;

@RestController
@RequestMapping("vegetablediseases")
public class VegetableDiseaseController extends CrudController<VegetableDisease, VegetableDiseaseDto, Long> {

	private final VegetableDiseaseService vegetableDiseaseService;
	private ModelMapper modelMapper;
	
	public VegetableDiseaseController(VegetableDiseaseService vegetableDiseaseService, ModelMapper modelMapper) {
		super(VegetableDisease.class,VegetableDiseaseDto.class);
		this.vegetableDiseaseService = vegetableDiseaseService;
		this.modelMapper = modelMapper;
	}
	
	@Override
	protected CrudService<VegetableDisease, Long> getService() {
		return this.vegetableDiseaseService;
	}

	@Override
	protected ModelMapper getModelMapper() {
		return this.modelMapper;
	}
}
