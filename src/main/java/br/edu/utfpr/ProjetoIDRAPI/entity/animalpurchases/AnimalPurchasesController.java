package br.edu.utfpr.ProjetoIDRAPI.entity.animalpurchases;

import br.edu.utfpr.ProjetoIDRAPI.entity.animalpurchases.dto.AnimalPurchasesDto;
import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudController;
import br.edu.utfpr.ProjetoIDRAPI.utils.GenericResponse;
import jakarta.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudService;

import java.util.List;

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

	@PostMapping("/sendPurchases")
	@ResponseStatus(HttpStatus.CREATED)
	public GenericResponse createRegister(@RequestBody @Valid List<AnimalPurchases> purchases) {
		animalPurchasesService.saveListPurchases(purchases);
		return new GenericResponse("Registros inseridos com sucesso");
	}
}
