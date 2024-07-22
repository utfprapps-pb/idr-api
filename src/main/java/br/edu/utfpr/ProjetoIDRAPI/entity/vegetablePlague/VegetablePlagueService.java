package br.edu.utfpr.ProjetoIDRAPI.entity.vegetablePlague;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudService;
import br.edu.utfpr.ProjetoIDRAPI.entity.vegetablePlague.VegetablePlague;

import java.util.List;

public interface VegetablePlagueService extends CrudService<VegetablePlague, Long> {
    boolean saveListVegetablePlagues(List<VegetablePlague> vegetablePlagues);

}
