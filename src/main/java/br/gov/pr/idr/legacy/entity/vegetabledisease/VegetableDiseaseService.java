package br.gov.pr.idr.legacy.entity.vegetabledisease;

import br.gov.pr.idr.legacy.entity.crud.CrudService;

import java.util.List;

public interface VegetableDiseaseService extends CrudService<VegetableDisease, Long> {
    boolean saveListVegetableDiseases(List<VegetableDisease> vegetableDiseases);

}
