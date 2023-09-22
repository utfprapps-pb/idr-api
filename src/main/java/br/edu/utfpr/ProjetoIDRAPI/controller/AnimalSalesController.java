package br.edu.utfpr.ProjetoIDRAPI.controller;

import br.edu.utfpr.ProjetoIDRAPI.utils.GenericResponse;
import jakarta.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import br.edu.utfpr.ProjetoIDRAPI.dto.AnimalSalesDto;
import br.edu.utfpr.ProjetoIDRAPI.model.AnimalSales;
import br.edu.utfpr.ProjetoIDRAPI.service.AnimalSalesService;
import br.edu.utfpr.ProjetoIDRAPI.service.CrudService;

import java.util.List;

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

	@PostMapping("/sendSales")
	@ResponseStatus(HttpStatus.CREATED)
	public GenericResponse createRegister(@RequestBody @Valid List<AnimalSales> animalSales) {
		System.out.println("animalSalesanimalSalesanimalSalesanimalSalesanimalSalesanimalSales");
		System.out.println(animalSales);
		animalSalesService.saveListAnimalSales(animalSales);
		return new GenericResponse("Registros inseridos com sucesso");
	}
}
