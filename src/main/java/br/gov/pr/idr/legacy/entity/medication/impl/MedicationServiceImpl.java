package br.gov.pr.idr.legacy.entity.medication.impl;

import br.gov.pr.idr.legacy.entity.animal.Animal;
import br.gov.pr.idr.legacy.entity.crud.impl.CrudServiceImpl;
import br.gov.pr.idr.legacy.entity.animal.AnimalService;
import br.gov.pr.idr.legacy.entity.medication.Medication;
import br.gov.pr.idr.legacy.entity.medication.MedicationRepository;
import br.gov.pr.idr.legacy.entity.medication.MedicationService;
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
