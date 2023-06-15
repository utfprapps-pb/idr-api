package br.edu.utfpr.ProjetoIDRAPI.service.impl;

import br.edu.utfpr.ProjetoIDRAPI.model.Animal;
import br.edu.utfpr.ProjetoIDRAPI.service.AnimalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import br.edu.utfpr.ProjetoIDRAPI.model.AnimalSales;
import br.edu.utfpr.ProjetoIDRAPI.repository.AnimalSalesRepository;
import br.edu.utfpr.ProjetoIDRAPI.service.AnimalSalesService;

import java.util.List;

@Service
@Slf4j
public class AnimalSalesServiceImpl extends CrudServiceImpl<AnimalSales, Long> implements AnimalSalesService {
	private final AnimalSalesRepository animalSalesRepository;
	private final AnimalService animalService;

	public AnimalSalesServiceImpl(AnimalSalesRepository animalSalesRepository, AnimalService animalService) {
		this.animalSalesRepository = animalSalesRepository;
		this.animalService = animalService;
	}

	@Override
	protected JpaRepository<AnimalSales, Long> getRepository() {
		return this.animalSalesRepository;
	}

	@Override
	public boolean saveListAnimalSales(List<AnimalSales> animalSales) {
		boolean status = true;
		try {
			for (AnimalSales sale : animalSales) {

				//Identifica o animal de acordo com o identifier
				String animalIdentifier = sale.getAnimal().getIdentifier();
				Animal animal = animalService.findByIdentifier(animalIdentifier);

				sale.setAnimal(animal);

				animalSalesRepository.save(sale);
			}
		} catch (Exception e){
			status = false;
			log.error(e.getMessage());
		}

		return status;
	}
}
