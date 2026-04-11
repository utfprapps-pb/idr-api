package br.edu.utfpr.ProjetoIDRAPI.entity.feed;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedRepository extends JpaRepository<Feed, Long>, FeedSpecExecutor {
	Feed findByName(String name);
}
