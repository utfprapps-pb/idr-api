package br.edu.utfpr.ProjetoIDRAPI.entity.email;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.utfpr.ProjetoIDRAPI.entity.email.Email;

public interface EmailRepository extends JpaRepository<Email, Long> {
	Email findByEmailTo(String emailTo);
}
