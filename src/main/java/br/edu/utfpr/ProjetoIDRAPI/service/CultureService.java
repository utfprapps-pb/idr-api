package br.edu.utfpr.ProjetoIDRAPI.service;

import br.edu.utfpr.ProjetoIDRAPI.model.Culture;

public interface CultureService extends CrudService<Culture, Long>{
	Culture findByName(String name);
}
