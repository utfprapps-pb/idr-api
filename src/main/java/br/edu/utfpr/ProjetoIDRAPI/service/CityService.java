package br.edu.utfpr.ProjetoIDRAPI.service;

import br.edu.utfpr.ProjetoIDRAPI.model.City;

public interface CityService extends CrudService<City, Long> {
	City findOneByNome(String name);
}
