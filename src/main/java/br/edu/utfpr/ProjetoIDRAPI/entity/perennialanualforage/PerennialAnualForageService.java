package br.edu.utfpr.ProjetoIDRAPI.entity.perennialanualforage;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudService;

import java.util.List;

public interface PerennialAnualForageService extends CrudService<PerennialAnualForage, Long> {

    List<PerennialAnualForage> findByPropertyId(Long id);

}
