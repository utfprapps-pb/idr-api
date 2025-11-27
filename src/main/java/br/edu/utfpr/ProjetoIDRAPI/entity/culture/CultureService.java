package br.edu.utfpr.ProjetoIDRAPI.entity.culture;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudService;
import br.edu.utfpr.ProjetoIDRAPI.entity.culture.Culture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CultureService extends CrudService<Culture, Long> {
	Culture findByName(String name);
	Page<Culture> search(CultureFilter filter, Pageable pageable);

}
