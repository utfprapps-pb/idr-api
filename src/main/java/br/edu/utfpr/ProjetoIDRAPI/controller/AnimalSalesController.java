package br.edu.utfpr.ProjetoIDRAPI.controller;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.utfpr.ProjetoIDRAPI.dto.AnimalSalesDto;
import br.edu.utfpr.ProjetoIDRAPI.model.AnimalSales;
import br.edu.utfpr.ProjetoIDRAPI.service.AnimalSalesService;
import br.edu.utfpr.ProjetoIDRAPI.service.CrudService;

@RestController
@RequestMapping("animalSales")
public class AnimalSalesController extends CrudController<AnimalSales, AnimalSalesDto, Long> {
	
	private final AnimalSalesService animalSalesService;
	private ModelMapper modelMapper;
	
	public AnimalSalesController(AnimalSalesService animalSalesService, ModelMapper modelMapper) {
		super(AnimalSales.class, AnimalSalesDto.class);
		this.animalSalesService = animalSalesService;
		this.modelMapper = modelMapper;
	}
	
	@Override
	protected CrudService<AnimalSales, Long> getService() {
		return this.animalSalesService;
	}
	
	@Override
	protected ModelMapper getModelMapper() {
		return this.modelMapper;
	}
}
