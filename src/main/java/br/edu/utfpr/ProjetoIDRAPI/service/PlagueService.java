package br.edu.utfpr.ProjetoIDRAPI.service;

import br.edu.utfpr.ProjetoIDRAPI.model.Plague;

public interface PlagueService extends CrudService<Plague, Long> {
	Plague findByName(String name);
}
