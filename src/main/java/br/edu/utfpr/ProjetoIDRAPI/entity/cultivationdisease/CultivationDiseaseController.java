package br.edu.utfpr.ProjetoIDRAPI.entity.cultivationdisease;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudController;
import br.edu.utfpr.ProjetoIDRAPI.entity.cultivationdisease.dto.CultivationDiseaseDto;
import br.edu.utfpr.ProjetoIDRAPI.utils.GenericResponse;
import jakarta.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudService;

import java.util.List;

@RestController
@RequestMapping("cultivation-diseases")
public class CultivationDiseaseController extends CrudController<CultivationDisease, CultivationDiseaseDto, Long> {

	private final CultivationDiseaseService cultivationDiseaseService;
	private final ModelMapper modelMapper;
	
	public CultivationDiseaseController(CultivationDiseaseService cultivationDiseaseService, ModelMapper modelMapper) {
		super(CultivationDisease.class, CultivationDiseaseDto.class);
		this.cultivationDiseaseService = cultivationDiseaseService;
		this.modelMapper = modelMapper;
	}
	
	@Override
	protected CrudService<CultivationDisease, Long> getService() {
		return this.cultivationDiseaseService;
	}

	@Override
	protected ModelMapper getModelMapper() {
		return this.modelMapper;
	}

	@PostMapping("all")
	@ResponseStatus(HttpStatus.CREATED)
	public GenericResponse createRegister(@RequestBody @Valid List<CultivationDisease> cultivationDiseases) {
		cultivationDiseaseService.saveListCultivationDiseases(cultivationDiseases);
		return new GenericResponse("Dados inseridos com sucesso");
	}
}
