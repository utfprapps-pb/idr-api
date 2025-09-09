package br.edu.utfpr.ProjetoIDRAPI.entity.vegetableplague;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudController;
import br.edu.utfpr.ProjetoIDRAPI.entity.vegetableplague.dto.VegetablePlagueDto;
import br.edu.utfpr.ProjetoIDRAPI.utils.GenericResponse;
import jakarta.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudService;

import java.util.List;

@RestController
@RequestMapping("vegetableplagues")
public class VegetablePlagueController extends CrudController<VegetablePlague, VegetablePlagueDto, Long> {

	private final VegetablePlagueService VegetablePlagueService;
	private ModelMapper modelMapper;
	
	public VegetablePlagueController(VegetablePlagueService VegetablePlagueService, ModelMapper modelMapper) {
		super(VegetablePlague.class,VegetablePlagueDto.class);
		this.VegetablePlagueService = VegetablePlagueService;
		this.modelMapper = modelMapper;
	}

	@Override
	protected CrudService<VegetablePlague, Long> getService() {
		return this.VegetablePlagueService;
	}

	@Override
	protected ModelMapper getModelMapper() {
		return this.modelMapper;
	}

	@PostMapping("/sendVegetablePlagues")
	@ResponseStatus(HttpStatus.CREATED)
	public GenericResponse createRegister(@RequestBody @Valid List<VegetablePlague> vegetablePlagues) {
		VegetablePlagueService.saveListVegetablePlagues(vegetablePlagues);
		return new GenericResponse("Registros inseridos com sucesso");
	}
}
