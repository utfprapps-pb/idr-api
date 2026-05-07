package br.gov.pr.idr.legacy.entity.plague;

import br.gov.pr.idr.legacy.entity.crud.CrudService;

public interface PlagueService extends CrudService<Plague, Long> {
	Plague findByName(String name);
}
