package br.edu.utfpr.ProjetoIDRAPI.entity.property;

import java.util.List;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudService;

public interface PropertyService extends CrudService<Property, Long> {

	List<Property> findByUserId(Long id);
}
