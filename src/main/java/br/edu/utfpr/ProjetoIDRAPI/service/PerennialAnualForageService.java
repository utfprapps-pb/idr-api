package br.edu.utfpr.ProjetoIDRAPI.service;

import br.edu.utfpr.ProjetoIDRAPI.model.PerennialAnualForage;

import java.util.List;

public interface PerennialAnualForageService extends CrudService<PerennialAnualForage, Long> {

    List<PerennialAnualForage> findByPropertyId(Long id);

}
