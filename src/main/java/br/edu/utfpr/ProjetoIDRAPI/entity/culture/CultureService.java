package br.edu.utfpr.ProjetoIDRAPI.entity.culture;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudService;
import br.edu.utfpr.ProjetoIDRAPI.entity.culture.Culture;

public interface CultureService extends CrudService<Culture, Long> {
	Culture findByName(String name);
}
