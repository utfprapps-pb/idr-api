package br.edu.utfpr.ProjetoIDRAPI.service;

import br.edu.utfpr.ProjetoIDRAPI.model.Disease;

public interface DiseaseService extends CrudService<Disease, Long> {
	Disease findByName(String name);
}
