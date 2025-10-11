package br.edu.utfpr.ProjetoIDRAPI.entity.culture;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudController;
import br.edu.utfpr.ProjetoIDRAPI.entity.culture.dto.CultureDto;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudService;

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

	@PostMapping("/advanced-search")
	public ResponseEntity<Page<CultureDto>> search(
			@RequestBody CultureFilter filters,
			Pageable pageable
	) {
		Page<Culture> page = cultureService.search(filters, pageable);
		return ResponseEntity.ok(page.map(this::convertToDto));
	}


}
