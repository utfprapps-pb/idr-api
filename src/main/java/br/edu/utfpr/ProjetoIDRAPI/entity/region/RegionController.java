package br.edu.utfpr.ProjetoIDRAPI.entity.region;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudController;
import br.edu.utfpr.ProjetoIDRAPI.entity.region.dto.RegionDto;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudService;

@RestController
@RequestMapping("regions")
public class RegionController extends CrudController<Region, RegionDto, Long> {

	private final RegionService regionService;
	private final ModelMapper modelMapper;
	
	public RegionController(RegionService regionService, ModelMapper modelMapper) {
		super(Region.class, RegionDto.class);
		this.regionService = regionService;
		this.modelMapper = modelMapper;
	}

	@Override
	protected CrudService<Region, Long> getService() {
		return this.regionService;
	}

	@Override
	protected ModelMapper getModelMapper() {
		return this.modelMapper;
	}
	
	@GetMapping("/findName/{name}")
	public ResponseEntity<Region> findByName(@PathVariable String name){
		Region entity = regionService.findByName(name);
		
		if(entity != null) {
			return ResponseEntity.ok(modelMapper.map(entity, Region.class));
		} else {
    		return ResponseEntity.noContent().build();
    	}
	}
}
