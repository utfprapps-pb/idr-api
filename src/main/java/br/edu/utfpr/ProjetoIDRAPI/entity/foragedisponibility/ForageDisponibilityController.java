package br.edu.utfpr.ProjetoIDRAPI.entity.foragedisponibility;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudController;
import br.edu.utfpr.ProjetoIDRAPI.entity.foragedisponibility.dto.ForageDisponibilityDto;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudService;

import java.util.List;

@RestController
@RequestMapping("/properties/{propertyId}/forages") // agora a base é /properties
public class ForageDisponibilityController extends CrudController<ForageDisponibility, ForageDisponibilityDto, Long> {

	private final ForageDisponibilityService forageService;
	private final ModelMapper modelMapper;

	public ForageDisponibilityController(ForageDisponibilityService forageService, ModelMapper modelMapper) {
		super(ForageDisponibility.class, ForageDisponibilityDto.class);
		this.forageService = forageService;
		this.modelMapper = modelMapper;
	}

	@Override
	protected CrudService<ForageDisponibility, Long> getService() {
		return this.forageService;
	}

	@Override
	protected ModelMapper getModelMapper() {
		return this.modelMapper;
	}
	//
	@GetMapping
	public ResponseEntity<List<ForageDisponibilityDto>> getByProperty(@PathVariable Long propertyId) {
		List<ForageDisponibilityDto> dtos = forageService.findByPropertyId(propertyId);
		return ResponseEntity.ok(dtos);
	}
}

