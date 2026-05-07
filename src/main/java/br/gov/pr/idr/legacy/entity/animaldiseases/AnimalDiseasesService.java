package br.gov.pr.idr.legacy.entity.animaldiseases;

import br.gov.pr.idr.legacy.entity.crud.CrudService;

import java.util.List;

public interface AnimalDiseasesService extends CrudService<AnimalDiseases, Long> {

    boolean saveListAnimalDiseases(List<AnimalDiseases> animalDiseasesList);

}
