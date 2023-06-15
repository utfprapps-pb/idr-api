package br.edu.utfpr.ProjetoIDRAPI.service.impl;

import br.edu.utfpr.ProjetoIDRAPI.model.Animal;
 import br.edu.utfpr.ProjetoIDRAPI.model.Mastitis;
import br.edu.utfpr.ProjetoIDRAPI.repository.MastitisRepository;
import br.edu.utfpr.ProjetoIDRAPI.service.AnimalService;
import br.edu.utfpr.ProjetoIDRAPI.service.MastitisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class MastitisServiceImpl extends CrudServiceImpl<Mastitis, Long> implements MastitisService {

    private final MastitisRepository mastitisRepository;
    private final AnimalService animalService;

    public MastitisServiceImpl(MastitisRepository mastitisRepository, AnimalService animalService) {
        this.mastitisRepository = mastitisRepository;
        this.animalService = animalService;
    }

    @Override
    protected JpaRepository<Mastitis, Long> getRepository() {
        return this.mastitisRepository;
    }

    @Override
    public boolean saveListMastitis(List<Mastitis> mastitisList) {
        boolean status = true;
        try {
            for (Mastitis mastitis : mastitisList) {

                //Identifica o animal de acordo com o identifier
                String animalIdentifier = mastitis.getAnimal().getIdentifier();
                Animal animal = animalService.findByIdentifier(animalIdentifier);

                mastitis.setAnimal(animal);

                mastitisRepository.save(mastitis);
            }
        } catch (Exception e){
            status = false;
            log.error(e.getMessage());
        }

        return status;
    }
}
