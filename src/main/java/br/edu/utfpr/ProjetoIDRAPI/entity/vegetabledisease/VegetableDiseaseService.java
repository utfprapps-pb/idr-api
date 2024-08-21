package br.edu.utfpr.ProjetoIDRAPI.entity.vegetabledisease;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudService;

import java.util.List;

public interface VegetableDiseaseService extends CrudService<VegetableDisease, Long> {
    boolean saveListVegetableDiseases(List<VegetableDisease> vegetableDiseases);

}
