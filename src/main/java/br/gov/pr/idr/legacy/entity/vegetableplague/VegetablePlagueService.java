package br.gov.pr.idr.legacy.entity.vegetableplague;

import br.gov.pr.idr.legacy.entity.crud.CrudService;

import java.util.List;

public interface VegetablePlagueService extends CrudService<VegetablePlague, Long> {
    boolean saveListVegetablePlagues(List<VegetablePlague> vegetablePlagues);

}
