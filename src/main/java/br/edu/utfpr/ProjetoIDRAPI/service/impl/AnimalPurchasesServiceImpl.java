package br.edu.utfpr.ProjetoIDRAPI.service.impl;

import br.edu.utfpr.ProjetoIDRAPI.model.Animal;
import br.edu.utfpr.ProjetoIDRAPI.service.AnimalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import br.edu.utfpr.ProjetoIDRAPI.model.AnimalPurchases;
import br.edu.utfpr.ProjetoIDRAPI.repository.AnimalPurchasesRepository;
import br.edu.utfpr.ProjetoIDRAPI.service.AnimalPurchasesService;

import java.util.List;

@Service
@Slf4j
public class AnimalPurchasesServiceImpl extends CrudServiceImpl<AnimalPurchases, Long> implements AnimalPurchasesService {
	private final AnimalPurchasesRepository animalPurchasesRepository;
	private final AnimalService animalService;

	public AnimalPurchasesServiceImpl(AnimalPurchasesRepository animalPurchasesRepository, AnimalService animalService) {
		this.animalPurchasesRepository = animalPurchasesRepository;
		this.animalService = animalService;

	}

	@Override
	protected JpaRepository<AnimalPurchases, Long> getRepository() {
		return this.animalPurchasesRepository;
	}

	@Override
	public boolean saveListPurchases(List<AnimalPurchases> purchases) {
		boolean status = true;
		try {
			for (AnimalPurchases purchase : purchases) {

				//Identifica o animal de acordo com o identifier
				String animalIdentifier = purchase.getAnimal().getIdentifier();
				Animal animal = animalService.findByIdentifier(animalIdentifier);

				purchase.setAnimal(animal);

				animalPurchasesRepository.save(purchase);
			}
		} catch (Exception e){
			status = false;
			log.error(e.getMessage());
		}

		return status;
	}
}
