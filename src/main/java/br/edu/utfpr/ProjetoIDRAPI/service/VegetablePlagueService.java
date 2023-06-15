package br.edu.utfpr.ProjetoIDRAPI.service;

import br.edu.utfpr.ProjetoIDRAPI.model.VegetablePlague;

import java.util.List;

public interface VegetablePlagueService extends CrudService<VegetablePlague, Long> {
    boolean saveListVegetablePlagues(List<VegetablePlague> vegetablePlagues);

}
