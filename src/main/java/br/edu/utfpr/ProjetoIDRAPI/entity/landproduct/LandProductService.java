package br.edu.utfpr.ProjetoIDRAPI.entity.landproduct;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudService;

import java.util.List;

public interface LandProductService extends CrudService<LandProduct, Long> {

    List<LandProduct> findByPropertyId(Long id);

}
