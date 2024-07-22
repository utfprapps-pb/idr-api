package br.edu.utfpr.ProjetoIDRAPI.entity.breed.impl;

import br.edu.utfpr.ProjetoIDRAPI.entity.breed.Breed;
import br.edu.utfpr.ProjetoIDRAPI.entity.breed.BreedRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.breed.BreedService;
import br.edu.utfpr.ProjetoIDRAPI.entity.crud.impl.CrudServiceImpl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

@Service
public class BreedServiceImpl extends CrudServiceImpl<Breed, Long> implements BreedService {
	private final BreedRepository breedRepository;
	
	public BreedServiceImpl(BreedRepository breedRepository) {
		this.breedRepository = breedRepository;
	}
	
	@Override
	protected JpaRepository<Breed, Long> getRepository() {
		return this.breedRepository;
	}

	@Override
	public JpaSpecificationExecutor<Breed> getSpecExecutor() {
		return this.breedRepository;
	}
}
