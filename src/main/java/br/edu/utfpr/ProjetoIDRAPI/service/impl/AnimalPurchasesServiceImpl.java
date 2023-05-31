package br.edu.utfpr.ProjetoIDRAPI.service.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import br.edu.utfpr.ProjetoIDRAPI.model.AnimalPurchases;
import br.edu.utfpr.ProjetoIDRAPI.repository.AnimalPurchasesRepository;
import br.edu.utfpr.ProjetoIDRAPI.service.AnimalPurchasesService;

@Service
public class AnimalPurchasesServiceImpl extends CrudServiceImpl<AnimalPurchases, Long> implements AnimalPurchasesService {
	private final AnimalPurchasesRepository animalPurchasesRepository;
	
	public AnimalPurchasesServiceImpl(AnimalPurchasesRepository animalPurchasesRepository) {
		this.animalPurchasesRepository = animalPurchasesRepository;
	}

	@Override
	protected JpaRepository<AnimalPurchases, Long> getRepository() {
		return this.animalPurchasesRepository;
	}
}
