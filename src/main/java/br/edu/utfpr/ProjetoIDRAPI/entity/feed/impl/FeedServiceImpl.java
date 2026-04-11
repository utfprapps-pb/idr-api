package br.edu.utfpr.ProjetoIDRAPI.entity.feed.impl;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.impl.CrudServiceImpl;
import br.edu.utfpr.ProjetoIDRAPI.entity.feed.Feed;
import br.edu.utfpr.ProjetoIDRAPI.entity.feed.FeedRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.feed.FeedService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

@Service
public class FeedServiceImpl extends CrudServiceImpl<Feed, Long> implements FeedService {
	private final FeedRepository feedRepository;
	
	public FeedServiceImpl(FeedRepository feedRepository) {
		this.feedRepository = feedRepository;
	}
	
	@Override
	public Feed findByName(String name) {
		return feedRepository.findByName(name);
	}

	@Override
	protected JpaRepository<Feed, Long> getRepository() {
		return this.feedRepository;
	}

	@Override
	public JpaSpecificationExecutor<Feed> getSpecExecutor() {
		return this.feedRepository;
	}
}
