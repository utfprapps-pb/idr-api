package br.gov.pr.idr.legacy.entity.landproduct;

import br.gov.pr.idr.legacy.entity.crud.CrudService;

import java.util.List;

public interface LandProductService extends CrudService<LandProduct, Long> {

    List<LandProduct> findByPropertyId(Long id);

}
