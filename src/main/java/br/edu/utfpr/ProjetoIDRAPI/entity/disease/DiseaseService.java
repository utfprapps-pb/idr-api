package br.edu.utfpr.ProjetoIDRAPI.entity.disease;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudService;

public interface DiseaseService extends CrudService<Disease, Long> {
	Disease findByName(String name);
}
