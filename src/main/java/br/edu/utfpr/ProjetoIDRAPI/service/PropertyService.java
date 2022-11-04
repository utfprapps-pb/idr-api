package br.edu.utfpr.ProjetoIDRAPI.service;

import java.util.List;

import br.edu.utfpr.ProjetoIDRAPI.model.Property;

public interface PropertyService extends CrudService<Property, Long>{
	List<Property> findByUserId(Long id);
}
