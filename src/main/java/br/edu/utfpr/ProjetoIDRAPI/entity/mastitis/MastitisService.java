package br.edu.utfpr.ProjetoIDRAPI.entity.mastitis;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudService;
import br.edu.utfpr.ProjetoIDRAPI.entity.mastitis.Mastitis;

import java.util.List;

public interface MastitisService extends CrudService<Mastitis, Long> {
    boolean saveListMastitis(List<Mastitis> mastitisList);

}
