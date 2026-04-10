package br.edu.utfpr.ProjetoIDRAPI.entity.city;

import br.edu.utfpr.ProjetoIDRAPI.entity.city.dto.CityDto;
import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudController;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudService;

@RestController
@RequestMapping("cities")
public class CityController extends CrudController<City, CityDto, Long> {

	private final CityService cityService;
	private final ModelMapper modelMapper;

	public CityController(CityService cityService, ModelMapper modelMapper) {
		super(City.class, CityDto.class);
		this.cityService = cityService;
		this.modelMapper = modelMapper;
	}

	@Override
	protected CrudService<City, Long> getService() {
		return this.cityService;
	}

	@Override
	protected ModelMapper getModelMapper() {
		return this.modelMapper;
	}

	@GetMapping("/findName/{name}")
	public ResponseEntity<CityDto> findByName(@PathVariable String name){
		City entity = cityService.findByName(name);
		
		if(entity != null) {
			return ResponseEntity.ok(modelMapper.map(entity, CityDto.class));
    	} else {
    		return ResponseEntity.noContent().build();
    	}
	}
}