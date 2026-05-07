package br.gov.pr.idr.legacy.entity.foragedisponibility;

import br.gov.pr.idr.legacy.entity.crud.CrudController;
import br.gov.pr.idr.legacy.entity.crud.CrudService;
import br.gov.pr.idr.legacy.entity.foragedisponibility.dto.ForageDisponibilityDto;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/properties/{propertyId}/forages") // base da rota do frontend
public class ForageDisponibilityController
		extends CrudController<ForageDisponibility, ForageDisponibilityDto, Long> {

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

	/**
	 * Endpoint para buscar todas as ForageDisponibility de uma propriedade
	 * @param propertyId ID da propriedade
	 * @return Lista de ForageDisponibilityDto compatível com o frontend
	 */
	@GetMapping
	public ResponseEntity<List<ForageDisponibilityDto>> getByProperty(@PathVariable Long propertyId) {
		List<ForageDisponibilityDto> dtos = forageService.findByPropertyId(propertyId);
		return ResponseEntity.ok(dtos);
	}


}
