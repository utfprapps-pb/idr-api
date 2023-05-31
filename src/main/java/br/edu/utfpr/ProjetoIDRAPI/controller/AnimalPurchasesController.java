package br.edu.utfpr.ProjetoIDRAPI.controller;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.utfpr.ProjetoIDRAPI.dto.AnimalPurchasesDto;
import br.edu.utfpr.ProjetoIDRAPI.model.AnimalPurchases;
import br.edu.utfpr.ProjetoIDRAPI.service.AnimalPurchasesService;
import br.edu.utfpr.ProjetoIDRAPI.service.CrudService;

@RestController
@RequestMapping("animalPurchases")
public class AnimalPurchasesController extends CrudController<AnimalPurchases, AnimalPurchasesDto, Long> {

	private final AnimalPurchasesService animalPurchasesService;
	private ModelMapper modelMapper;
	
	public AnimalPurchasesController(AnimalPurchasesService animalPurchasesService, ModelMapper modelMapper) {
		super(AnimalPurchases.class, AnimalPurchasesDto.class);
		this.animalPurchasesService = animalPurchasesService;
		this.modelMapper = modelMapper;
	}
	
	@Override
	protected CrudService<AnimalPurchases, Long> getService() {
		return this.animalPurchasesService;
	}
	
	@Override
	protected ModelMapper getModelMapper() {
		return this.modelMapper;
	}
}
