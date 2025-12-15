package br.edu.utfpr.ProjetoIDRAPI.entity.breed;

import br.edu.utfpr.ProjetoIDRAPI.entity.breed.dto.BreedDto;
import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudController;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudService;

@RestController
@RequestMapping("breeds")
public class BreedController extends CrudController<Breed, BreedDto, Long> {
	private final BreedService breedService;
	private ModelMapper modelMopper;
	
	public BreedController(BreedService breedService, ModelMapper modelMopper) {
		super(Breed.class, BreedDto.class);
		this.breedService = breedService;
		this.modelMopper = modelMopper;
	}
	
	@Override
	protected CrudService<Breed, Long> getService() {
		return this.breedService;
	}

	@Override
	protected ModelMapper getModelMapper() {
		return this.modelMopper;
	}

}
