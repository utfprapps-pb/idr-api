package br.edu.utfpr.ProjetoIDRAPI.controller;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.utfpr.ProjetoIDRAPI.dto.PlagueDto;
import br.edu.utfpr.ProjetoIDRAPI.model.Plague;
import br.edu.utfpr.ProjetoIDRAPI.service.CrudService;
import br.edu.utfpr.ProjetoIDRAPI.service.PlagueService;

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
