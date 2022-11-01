package br.edu.utfpr.ProjetoIDRAPI.service;

import java.util.List;

import br.edu.utfpr.ProjetoIDRAPI.model.City;

public interface CityService {
	City findOneById(Long id);
	City findOneByNome(String name);
	List<City> findAll();
}
