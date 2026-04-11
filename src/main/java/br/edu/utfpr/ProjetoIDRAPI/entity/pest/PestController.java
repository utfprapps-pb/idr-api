package br.edu.utfpr.ProjetoIDRAPI.entity.pest;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudController;
import br.edu.utfpr.ProjetoIDRAPI.entity.pest.dto.PestDto;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudService;

@RestController
@RequestMapping("pests")
public class PestController extends CrudController<Pest, PestDto, Long> {
	private final PestService pestService;
	private final ModelMapper modelMapper;
	
	public PestController(PestService pestService, ModelMapper modelMapper) {
		super(Pest.class, PestDto.class);
		this.pestService = pestService;
		this.modelMapper = modelMapper;
	}
	
	@Override
	protected CrudService<Pest, Long> getService() {
		return this.pestService;
	}
	
	@Override
	protected ModelMapper getModelMapper() {
		return this.modelMapper;
	}
	
	@GetMapping("/findName/{name}")
	public ResponseEntity<PestDto> findByName(@PathVariable String name){
		Pest entity = pestService.findByName(name);
		
		if(entity != null) {
			return ResponseEntity.ok(super.convertToDto(pestService.findByName(name)));
		} else {
    		return ResponseEntity.noContent().build();
    	}
	}
}
