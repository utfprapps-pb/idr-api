package br.edu.utfpr.ProjetoIDRAPI.service.impl;

import br.edu.utfpr.ProjetoIDRAPI.model.Animal;
import br.edu.utfpr.ProjetoIDRAPI.model.Medication;
import br.edu.utfpr.ProjetoIDRAPI.repository.MedicationRepository;
import br.edu.utfpr.ProjetoIDRAPI.service.AnimalService;
import br.edu.utfpr.ProjetoIDRAPI.service.MedicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class MedicationServiceImpl extends CrudServiceImpl<Medication, Long> implements MedicationService {

    private final MedicationRepository medicationRepository;
    private final AnimalService animalService;

    public MedicationServiceImpl(MedicationRepository medicationRepository, AnimalService animalService) {
        this.medicationRepository = medicationRepository;
        this.animalService = animalService;
    }

    @Override
    protected JpaRepository<Medication, Long> getRepository() {
        return this.medicationRepository;
    }

    @Override
    public boolean saveListMedications(List<Medication> medications) {
        boolean status = true;
        try {
            for (Medication medication : medications) {
                System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
                System.out.println(medication);

                //Identifica o animal de acordo com o identifier
                String animalIdentifier = medication.getAnimal().getIdentifier();
                Animal animal = animalService.findByIdentifier(animalIdentifier);

                medication.setAnimal(animal);

                medicationRepository.save(medication);
            }
        } catch (Exception e){
            status = false;
            log.error(e.getMessage());
        }

        return status;
    }
}
