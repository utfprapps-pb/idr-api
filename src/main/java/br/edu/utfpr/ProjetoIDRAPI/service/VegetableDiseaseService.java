package br.edu.utfpr.ProjetoIDRAPI.service;

import br.edu.utfpr.ProjetoIDRAPI.model.VegetableDisease;

import java.util.List;

public interface VegetableDiseaseService extends CrudService<VegetableDisease, Long> {
    boolean saveListVegetableDiseases(List<VegetableDisease> vegetableDiseases);

}
