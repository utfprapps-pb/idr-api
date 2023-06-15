package br.edu.utfpr.ProjetoIDRAPI.service;

import br.edu.utfpr.ProjetoIDRAPI.model.Mastitis;

import java.util.List;

public interface MastitisService extends CrudService<Mastitis, Long> {
    boolean saveListMastitis(List<Mastitis> mastitisList);

}
