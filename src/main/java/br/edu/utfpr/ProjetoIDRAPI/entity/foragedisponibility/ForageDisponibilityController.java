package br.edu.utfpr.ProjetoIDRAPI.entity.foragedisponibility;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudController;
import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudService;
import br.edu.utfpr.ProjetoIDRAPI.entity.foragedisponibility.dto.ForageCreateDto;
import br.edu.utfpr.ProjetoIDRAPI.entity.foragedisponibility.dto.ForageDisponibilityDto;
import br.edu.utfpr.ProjetoIDRAPI.entity.foragedisponibility.dto.ForageUpdateDto;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/properties/{propertyId}/forages") // base da rota do frontend
public class ForageDisponibilityController
		extends CrudController<ForageDisponibility, ForageDisponibilityDto, Long> {

	private static final Logger log = LoggerFactory.getLogger(ForageDisponibilityController.class);

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

	@Override
	@RequestMapping(method = RequestMethod.POST, value = "/_ignored_")
	@Deprecated
	public ResponseEntity<Long> create(ForageDisponibilityDto dto) {
		throw new UnsupportedOperationException("O endpoint de criação de foragem usa o método customizado 'createForageForProperty'.");
	}

	@GetMapping
	public ResponseEntity<List<ForageDisponibilityDto>> getByProperty(@PathVariable Long propertyId) {
		List<ForageDisponibilityDto> dtos = forageService.findByPropertyId(propertyId);
		return ResponseEntity.ok(dtos);
	}

	@PostMapping // Endpoint: POST /properties/{propertyId}/forages
	public ResponseEntity<ForageDisponibilityDto> createForageForProperty(
			@PathVariable Long propertyId,
			@RequestBody @Valid ForageCreateDto createDto) {
		try {
			ForageDisponibility createdForage = forageService.createForage(propertyId, createDto);
			ForageDisponibilityDto responseDto = modelMapper.map(createdForage, ForageDisponibilityDto.class);
			return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
		} catch (Exception e) {
			log.error("Erro na criação da Foragem: {}", e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	@PatchMapping("/{forageId}")
	public ResponseEntity<Void> updateForage(
			@PathVariable Long propertyId,
			@PathVariable Long forageId,
			@RequestBody @Valid ForageUpdateDto updateDto) {
		forageService.updateForage(propertyId, forageId, updateDto);
		return ResponseEntity.noContent().build(); // status 204
	}

	@GetMapping("/{id}/details")
	public ResponseEntity<ForageDisponibilityDto> findById(@PathVariable Long id) {
		ForageDisponibilityDto forageDto = forageService.findDtoById(id);
		return ResponseEntity.ok(forageDto);
	}
}
