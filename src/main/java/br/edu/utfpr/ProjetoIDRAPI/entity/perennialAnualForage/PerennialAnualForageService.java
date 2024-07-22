package br.edu.utfpr.ProjetoIDRAPI.entity.perennialAnualForage;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudService;
import br.edu.utfpr.ProjetoIDRAPI.entity.perennialAnualForage.PerennialAnualForage;

import java.util.List;

public interface PerennialAnualForageService extends CrudService<PerennialAnualForage, Long> {

    List<PerennialAnualForage> findByPropertyId(Long id);

}
