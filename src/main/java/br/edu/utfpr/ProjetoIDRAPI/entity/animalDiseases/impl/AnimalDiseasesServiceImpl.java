package br.edu.utfpr.ProjetoIDRAPI.entity.animalDiseases.impl;

import br.edu.utfpr.ProjetoIDRAPI.entity.animal.Animal;
import br.edu.utfpr.ProjetoIDRAPI.entity.animal.AnimalService;
import br.edu.utfpr.ProjetoIDRAPI.entity.animalDiseases.AnimalDiseases;
import br.edu.utfpr.ProjetoIDRAPI.entity.animalDiseases.AnimalDiseasesRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.animalDiseases.AnimalDiseasesService;
import br.edu.utfpr.ProjetoIDRAPI.entity.crud.impl.CrudServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AnimalDiseasesServiceImpl extends CrudServiceImpl<AnimalDiseases, Long> implements AnimalDiseasesService {
	private final AnimalDiseasesRepository animalDiseasesRepository;
	private final AnimalService animalService;

	public AnimalDiseasesServiceImpl(AnimalDiseasesRepository animalDiseasesRepository, AnimalService animalService) {
		this.animalDiseasesRepository = animalDiseasesRepository;
		this.animalService = animalService;
	}

	@Override
	protected JpaRepository<AnimalDiseases, Long> getRepository() {
		return this.animalDiseasesRepository;
	}

	@Override
	public boolean saveListAnimalDiseases(List<AnimalDiseases> animalDiseasesList) {
			boolean status = true;
			try {
				for (AnimalDiseases animalDiseases : animalDiseasesList) {

					//Identifica o animal de acordo com o
					String animalIdentifier = animalDiseases.getAnimal().getIdentifier();
					Animal animal = animalService.findByIdentifier(animalIdentifier);

					animalDiseases.setAnimal(animal);

					animalDiseasesRepository.save(animalDiseases);
				}
			} catch (Exception e){
				status = false;
				log.error(e.getMessage());
			}

			return status;
		}
}
