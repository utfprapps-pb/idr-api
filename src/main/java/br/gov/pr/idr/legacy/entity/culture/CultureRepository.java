package br.gov.pr.idr.legacy.entity.culture;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CultureRepository extends JpaRepository<Culture, Long>, CultureSpecExecutor {
	Culture findByCultureName(String name);
}
