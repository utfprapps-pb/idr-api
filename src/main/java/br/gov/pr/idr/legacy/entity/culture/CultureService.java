package br.gov.pr.idr.legacy.entity.culture;

import br.gov.pr.idr.legacy.entity.crud.CrudService;

public interface CultureService extends CrudService<Culture, Long> {
	Culture findByName(String name);
}
