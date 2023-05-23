package br.edu.utfpr.ProjetoIDRAPI.service;

import br.edu.utfpr.ProjetoIDRAPI.model.LandProduct;

import java.util.List;

public interface LandProductService extends CrudService<LandProduct, Long> {

    List<LandProduct> findByPropertyId(Long id);

}
