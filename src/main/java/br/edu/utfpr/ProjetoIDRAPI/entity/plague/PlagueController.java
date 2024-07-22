package br.edu.utfpr.ProjetoIDRAPI.entity.plague;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudController;
import br.edu.utfpr.ProjetoIDRAPI.entity.plague.dto.PlagueDto;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudService;

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
