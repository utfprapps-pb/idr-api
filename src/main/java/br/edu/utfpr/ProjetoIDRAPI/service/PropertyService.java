package br.edu.utfpr.ProjetoIDRAPI.service;

import java.util.List;

import br.edu.utfpr.ProjetoIDRAPI.model.Property;

public interface PropertyService {

	Property save(Property property);

	Property findOne(Long id);

	List<Property> findAll();

	List<Property> findByUserId(Long id);

	void delete(Long id);

}
