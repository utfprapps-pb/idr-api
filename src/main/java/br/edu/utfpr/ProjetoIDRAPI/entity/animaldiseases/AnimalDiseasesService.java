package br.edu.utfpr.ProjetoIDRAPI.entity.animaldiseases;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudService;

import java.util.List;

public interface AnimalDiseasesService extends CrudService<AnimalDiseases, Long> {

    boolean saveListAnimalDiseases(List<AnimalDiseases> animalDiseasesList);

}
