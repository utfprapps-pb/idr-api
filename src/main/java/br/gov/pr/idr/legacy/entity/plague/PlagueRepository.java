package br.gov.pr.idr.legacy.entity.plague;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PlagueRepository extends JpaRepository<Plague, Long>, PlagueSpecExecutor {
	Plague findByPlagueName(String name);
}
