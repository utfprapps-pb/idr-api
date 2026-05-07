package br.gov.pr.idr.legacy.entity.animalpurchases.impl;

import br.gov.pr.idr.legacy.entity.animal.Animal;
import br.gov.pr.idr.legacy.entity.animal.AnimalService;
import br.gov.pr.idr.legacy.entity.animalpurchases.AnimalPurchases;
import br.gov.pr.idr.legacy.entity.animalpurchases.AnimalPurchasesRepository;
import br.gov.pr.idr.legacy.entity.animalpurchases.AnimalPurchasesService;
import br.gov.pr.idr.legacy.entity.crud.impl.CrudServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

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
