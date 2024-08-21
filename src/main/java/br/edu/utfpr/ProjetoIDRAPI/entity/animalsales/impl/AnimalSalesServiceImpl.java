package br.edu.utfpr.ProjetoIDRAPI.entity.animalsales.impl;

import br.edu.utfpr.ProjetoIDRAPI.entity.animal.Animal;
import br.edu.utfpr.ProjetoIDRAPI.entity.animal.AnimalService;
import br.edu.utfpr.ProjetoIDRAPI.entity.animalsales.AnimalSales;
import br.edu.utfpr.ProjetoIDRAPI.entity.animalsales.AnimalSalesRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.animalsales.AnimalSalesService;
import br.edu.utfpr.ProjetoIDRAPI.entity.crud.impl.CrudServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

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
