package br.edu.utfpr.ProjetoIDRAPI.service.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import br.edu.utfpr.ProjetoIDRAPI.model.AnimalDiseases;
import br.edu.utfpr.ProjetoIDRAPI.repository.AnimalDiseasesRepository;
import br.edu.utfpr.ProjetoIDRAPI.service.AnimalDiseasesService;

@Service
public class AnimalDiseasesServiceImpl extends CrudServiceImpl<AnimalDiseases, Long> implements AnimalDiseasesService {
	private final AnimalDiseasesRepository animalDiseasesRepository;
	
	public AnimalDiseasesServiceImpl(AnimalDiseasesRepository animalDiseasesRepository) {
		this.animalDiseasesRepository = animalDiseasesRepository;
	}

	@Override
	protected JpaRepository<AnimalDiseases, Long> getRepository() {
		return this.animalDiseasesRepository;
	}
}
