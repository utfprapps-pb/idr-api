package br.edu.utfpr.ProjetoIDRAPI.entity.landProduct;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudService;
import br.edu.utfpr.ProjetoIDRAPI.entity.landProduct.LandProduct;

import java.util.List;

public interface LandProductService extends CrudService<LandProduct, Long> {

    List<LandProduct> findByPropertyId(Long id);

}
