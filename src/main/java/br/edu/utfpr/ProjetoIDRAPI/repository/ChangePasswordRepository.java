package br.edu.utfpr.ProjetoIDRAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.utfpr.ProjetoIDRAPI.model.ChangePassword;

public interface ChangePasswordRepository extends JpaRepository<ChangePassword, Long>{
	ChangePassword findByRecuperationEmailAndRecuperationCode(String recuperationEmail, String recuperationCode);
}
