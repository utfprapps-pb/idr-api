package br.edu.utfpr.ProjetoIDRAPI.entity.vegetabledisease;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudController;
import br.edu.utfpr.ProjetoIDRAPI.entity.vegetabledisease.dto.VegetableDiseaseDto;
import br.edu.utfpr.ProjetoIDRAPI.utils.GenericResponse;
import jakarta.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudService;

import java.util.List;

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

	@PostMapping("/sendVegetableDiseases")
	@ResponseStatus(HttpStatus.CREATED)
	public GenericResponse createRegister(@RequestBody @Valid List<VegetableDisease> vegetableDiseases) {
		vegetableDiseaseService.saveListVegetableDiseases(vegetableDiseases);
		return new GenericResponse("Registros inseridos com sucesso");
	}
}
