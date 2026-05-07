package br.gov.pr.idr.legacy.entity.culture;

import br.gov.pr.idr.legacy.entity.crud.CrudController;
import br.gov.pr.idr.legacy.entity.culture.dto.CultureDto;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.gov.pr.idr.legacy.entity.crud.CrudService;

@RestController
@RequestMapping("cultures")
public class CultureController extends CrudController<Culture, CultureDto, Long> {
	private final CultureService cultureService;
	private ModelMapper modelMapper;
	
	public CultureController(CultureService cultureService, ModelMapper modelMapper) {
		super(Culture.class, CultureDto.class);
		this.cultureService = cultureService;
		this.modelMapper = modelMapper;
	}

	@Override
	protected CrudService<Culture, Long> getService() {
		return this.cultureService;
	}

	@Override
	protected ModelMapper getModelMapper() {
		return this.modelMapper;
	}
	
	@GetMapping("/findName/{name}")
	public ResponseEntity<CultureDto> findByName(@PathVariable String name){
		Culture entity = cultureService.findByName(name);
		
		if(entity != null) {
			return ResponseEntity.ok(super.convertToDto(cultureService.findByName(name)));
		} else {
    		return ResponseEntity.noContent().build();
    	}
	}
}
