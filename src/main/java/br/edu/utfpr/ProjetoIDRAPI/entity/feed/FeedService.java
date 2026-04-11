package br.edu.utfpr.ProjetoIDRAPI.entity.feed;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudService;

public interface FeedService extends CrudService<Feed, Long> {
	Feed findByName(String name);
}
