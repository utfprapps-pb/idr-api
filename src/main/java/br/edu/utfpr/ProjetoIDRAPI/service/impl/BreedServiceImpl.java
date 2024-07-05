package br.edu.utfpr.ProjetoIDRAPI.service.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

import br.edu.utfpr.ProjetoIDRAPI.model.Breed;
import br.edu.utfpr.ProjetoIDRAPI.repository.BreedRepository;
import br.edu.utfpr.ProjetoIDRAPI.service.BreedService;

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
