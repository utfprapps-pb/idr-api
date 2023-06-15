package br.edu.utfpr.ProjetoIDRAPI.service.impl;

import br.edu.utfpr.ProjetoIDRAPI.model.Animal;
import br.edu.utfpr.ProjetoIDRAPI.model.PregnancyDiagnose;
import br.edu.utfpr.ProjetoIDRAPI.repository.PregnancyDiagnoseRepository;
import br.edu.utfpr.ProjetoIDRAPI.service.AnimalService;
import br.edu.utfpr.ProjetoIDRAPI.service.PregnancyDiagnoseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PregnancyDiagnoseServiceImpl extends CrudServiceImpl<PregnancyDiagnose, Long> implements PregnancyDiagnoseService {

    private final PregnancyDiagnoseRepository diagnoseRepository;
    private final AnimalService animalService;

    public PregnancyDiagnoseServiceImpl(PregnancyDiagnoseRepository diagnoseRepository, AnimalService animalService) {
        this.diagnoseRepository = diagnoseRepository;
        this.animalService = animalService;
    }

    @Override
    protected JpaRepository<PregnancyDiagnose, Long> getRepository() {
        return this.diagnoseRepository;
    }

    @Override
    public boolean saveListPregnancyDiagnoses(List<PregnancyDiagnose> pregnancyDiagnoseList) {
        boolean status = true;
        try {
            for (PregnancyDiagnose pregnancyDiagnose : pregnancyDiagnoseList) {

                //Identifica o animal de acordo com o identifier
                String animalIdentifier = pregnancyDiagnose.getAnimal().getIdentifier();
                Animal animal = animalService.findByIdentifier(animalIdentifier);

                pregnancyDiagnose.setAnimal(animal);

                diagnoseRepository.save(pregnancyDiagnose);
            }
        } catch (Exception e){
            status = false;
            log.error(e.getMessage());
        }

        return status;
    }
}
