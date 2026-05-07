package br.gov.pr.idr.legacy.entity.breed.impl;

import br.gov.pr.idr.legacy.entity.breed.Breed;
import br.gov.pr.idr.legacy.entity.breed.BreedRepository;
import br.gov.pr.idr.legacy.entity.breed.BreedService;
import br.gov.pr.idr.legacy.entity.crud.impl.CrudServiceImpl;
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
