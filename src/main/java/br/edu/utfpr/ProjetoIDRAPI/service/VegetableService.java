package br.edu.utfpr.ProjetoIDRAPI.service;

import br.edu.utfpr.ProjetoIDRAPI.model.Vegetable;

public interface VegetableService extends CrudService<Vegetable, Long>{
	Vegetable findByName(String name);
}
