package br.edu.utfpr.ProjetoIDRAPI.entity.vegetableplague;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudService;

import java.util.List;

public interface VegetablePlagueService extends CrudService<VegetablePlague, Long> {
    boolean saveListVegetablePlagues(List<VegetablePlague> vegetablePlagues);

}
