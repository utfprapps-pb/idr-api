package br.edu.utfpr.ProjetoIDRAPI.service;

import br.edu.utfpr.ProjetoIDRAPI.model.PerennialAnualFodders;

import java.util.List;

public interface PerennialAnualFoddersService extends CrudService<PerennialAnualFodders, Long> {
    List<PerennialAnualFodders> findByPropertyId(Long id);
}
