package br.edu.utfpr.ProjetoIDRAPI.service;

import br.edu.utfpr.ProjetoIDRAPI.model.AnimalDiseases;

import java.util.List;

public interface AnimalDiseasesService extends CrudService<AnimalDiseases, Long> {

    boolean saveListAnimalDiseases(List<AnimalDiseases> animalDiseasesList);

}
