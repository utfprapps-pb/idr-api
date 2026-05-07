package br.gov.pr.idr.legacy.entity.disease;

import br.gov.pr.idr.legacy.entity.crud.CrudService;

public interface DiseaseService extends CrudService<Disease, Long> {
	Disease findByName(String name);
}
