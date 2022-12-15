package br.edu.utfpr.ProjetoIDRAPI.service;

import br.edu.utfpr.ProjetoIDRAPI.model.ProductUse;

import java.util.List;

public interface ProductUseService extends CrudService<ProductUse, Long> {
    List<ProductUse> findByPropertyId(Long id);
}
