package br.edu.utfpr.ProjetoIDRAPI.entity.cultivationdisease;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudService;

import java.util.List;

public interface CultivationDiseaseService extends CrudService<CultivationDisease, Long> {

    boolean saveListCultivationDiseases(List<CultivationDisease> cultivationDiseases);

}
