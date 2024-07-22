package br.edu.utfpr.ProjetoIDRAPI.entity.vegetableDisease;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudService;
import br.edu.utfpr.ProjetoIDRAPI.entity.vegetableDisease.VegetableDisease;

import java.util.List;

public interface VegetableDiseaseService extends CrudService<VegetableDisease, Long> {
    boolean saveListVegetableDiseases(List<VegetableDisease> vegetableDiseases);

}
