package br.edu.utfpr.ProjetoIDRAPI.entity.cultivationpest;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudController;
import br.edu.utfpr.ProjetoIDRAPI.entity.cultivationpest.dto.CultivationPestDto;
import br.edu.utfpr.ProjetoIDRAPI.utils.GenericResponse;
import jakarta.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudService;

import java.util.List;

@RestController
@RequestMapping("cultivation-pests")
public class CultivationPestController extends CrudController<CultivationPest, CultivationPestDto, Long> {

	private final CultivationPestService CultivationPestService;
	private final ModelMapper modelMapper;
	
	public CultivationPestController(CultivationPestService CultivationPestService, ModelMapper modelMapper) {
		super(CultivationPest.class, CultivationPestDto.class);
		this.CultivationPestService = CultivationPestService;
		this.modelMapper = modelMapper;
	}

	@Override
	protected CrudService<CultivationPest, Long> getService() {
		return this.CultivationPestService;
	}

	@Override
	protected ModelMapper getModelMapper() {
		return this.modelMapper;
	}

	@PostMapping("all")
	@ResponseStatus(HttpStatus.CREATED)
	public GenericResponse createRegister(@RequestBody @Valid List<CultivationPest> cultivationPests) {
		CultivationPestService.saveListCultivationPest(cultivationPests);
		return new GenericResponse("Dados inseridos com sucesso");
	}
}
