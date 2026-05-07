package br.gov.pr.idr.legacy.entity.animalsales;

import br.gov.pr.idr.legacy.entity.animalsales.dto.AnimalSalesDto;
import br.gov.pr.idr.legacy.entity.crud.CrudController;
import br.gov.pr.idr.legacy.utils.GenericResponse;
import jakarta.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import br.gov.pr.idr.legacy.entity.crud.CrudService;

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
