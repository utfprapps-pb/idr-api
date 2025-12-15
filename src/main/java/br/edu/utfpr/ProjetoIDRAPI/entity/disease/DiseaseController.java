package br.edu.utfpr.ProjetoIDRAPI.entity.disease;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudController;
import br.edu.utfpr.ProjetoIDRAPI.entity.disease.dto.DiseaseDto;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudService;

@RestController
@RequestMapping("diseases")
public class DiseaseController extends CrudController<Disease, DiseaseDto, Long> {
	private final DiseaseService diseaseService;
	private ModelMapper modelMapper;
	
	public DiseaseController(DiseaseService diseaseService, ModelMapper modelMapper) {
		super(Disease.class, DiseaseDto.class);
		this.diseaseService = diseaseService;
		this.modelMapper = modelMapper;
	}

	@Override
	protected CrudService<Disease, Long> getService() {
		return this.diseaseService;
	}

	@Override
	protected ModelMapper getModelMapper() {
		return this.modelMapper;
	}
	
	@GetMapping("/findName/{name}")
	public ResponseEntity<DiseaseDto> findByName(@PathVariable String name){
		Disease entity = diseaseService.findByName(name);
		
		if(entity != null) {
			return ResponseEntity.ok(super.convertToDto(diseaseService.findByName(name)));
		} else {
    		return ResponseEntity.noContent().build();
    	}
	}
}
