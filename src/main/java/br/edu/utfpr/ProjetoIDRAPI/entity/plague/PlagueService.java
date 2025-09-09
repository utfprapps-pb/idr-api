package br.edu.utfpr.ProjetoIDRAPI.entity.plague;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudService;
import br.edu.utfpr.ProjetoIDRAPI.entity.plague.Plague;

public interface PlagueService extends CrudService<Plague, Long> {
	Plague findByName(String name);
}
