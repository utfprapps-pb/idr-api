package br.gov.pr.idr.legacy.entity.perennialanualforage;

import br.gov.pr.idr.legacy.entity.crud.CrudService;

import java.util.List;

public interface PerennialAnualForageService extends CrudService<PerennialAnualForage, Long> {

    List<PerennialAnualForage> findByPropertyId(Long id);

}
