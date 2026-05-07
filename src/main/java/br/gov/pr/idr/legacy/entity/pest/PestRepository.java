package br.gov.pr.idr.legacy.entity.pest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PestRepository extends JpaRepository<Pest, Long>, PestSpecExecutor {
}
