package br.edu.utfpr.ProjetoIDRAPI.entity.cultivationpest;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudService;

import java.util.List;

public interface CultivationPestService extends CrudService<CultivationPest, Long> {
    boolean saveListCultivationPest(List<CultivationPest> cultivationPests);

}
