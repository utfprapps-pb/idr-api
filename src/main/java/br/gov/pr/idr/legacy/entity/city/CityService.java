package br.gov.pr.idr.legacy.entity.city;

import br.gov.pr.idr.legacy.entity.crud.CrudService;

public interface CityService extends CrudService<City, Long> {

	City findByName(String name);

}
