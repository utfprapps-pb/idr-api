package br.gov.pr.idr.legacy.entity.email;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<Email, Long> {
	Email findByEmailTo(String emailTo);
}
