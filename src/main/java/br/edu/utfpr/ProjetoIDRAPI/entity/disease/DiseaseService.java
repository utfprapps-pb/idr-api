package br.edu.utfpr.ProjetoIDRAPI.entity.disease;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudService;
import br.edu.utfpr.ProjetoIDRAPI.entity.disease.Disease;

public interface DiseaseService extends CrudService<Disease, Long> {
	Disease findByName(String name);
}
