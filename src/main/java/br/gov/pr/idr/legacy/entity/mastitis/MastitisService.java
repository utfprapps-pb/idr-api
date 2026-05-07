package br.gov.pr.idr.legacy.entity.mastitis;

import br.gov.pr.idr.legacy.entity.crud.CrudService;

import java.util.List;

public interface MastitisService extends CrudService<Mastitis, Long> {
    boolean saveListMastitis(List<Mastitis> mastitisList);

}
