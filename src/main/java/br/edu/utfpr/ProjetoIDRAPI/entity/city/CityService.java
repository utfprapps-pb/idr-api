package br.edu.utfpr.ProjetoIDRAPI.entity.city;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudService;

public interface CityService extends CrudService<City, Long> {

	City findByName(String name);

}
