package br.edu.utfpr.ProjetoIDRAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.utfpr.ProjetoIDRAPI.model.Email;

public interface EmailRepository extends JpaRepository<Email, Long> {
	Email findByEmailTo(String emailTo);
}
