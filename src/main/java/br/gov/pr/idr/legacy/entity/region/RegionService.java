package br.gov.pr.idr.legacy.entity.region;

import br.gov.pr.idr.legacy.entity.crud.CrudService;

public interface RegionService extends CrudService<Region, Long> {

	Region findByName(String name);

}
