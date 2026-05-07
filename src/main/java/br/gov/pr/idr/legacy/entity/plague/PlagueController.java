package br.gov.pr.idr.legacy.entity.plague;

import br.gov.pr.idr.legacy.entity.crud.CrudController;
import br.gov.pr.idr.legacy.entity.plague.dto.PlagueDto;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.gov.pr.idr.legacy.entity.crud.CrudService;

@RestController
@RequestMapping("plagues")
public class PlagueController extends CrudController<Plague, PlagueDto, Long> {
	private final PlagueService plagueService;
	private ModelMapper modelMapper;
	
	public PlagueController(PlagueService plagueService, ModelMapper modelMapper) {
		super(Plague.class, PlagueDto.class);
		this.plagueService = plagueService;
		this.modelMapper = modelMapper;
	}
	
	@Override
	protected CrudService<Plague, Long> getService() {
		return this.plagueService;
	}
	
	@Override
	protected ModelMapper getModelMapper() {
		return this.modelMapper;
	}
	
	@GetMapping("/findName/{name}")
	public ResponseEntity<PlagueDto> findByName(@PathVariable String name){
		Plague entity = plagueService.findByName(name);
		
		if(entity != null) {
			return ResponseEntity.ok(super.convertToDto(plagueService.findByName(name)));
		} else {
    		return ResponseEntity.noContent().build();
    	}
	}
}
