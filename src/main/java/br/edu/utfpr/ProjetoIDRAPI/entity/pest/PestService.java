package br.edu.utfpr.ProjetoIDRAPI.entity.pest;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudService;

public interface PestService extends CrudService<Pest, Long> {
	Pest findByName(String name);
}
