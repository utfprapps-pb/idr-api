package br.edu.utfpr.ProjetoIDRAPI.entity.changePassword;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.utfpr.ProjetoIDRAPI.entity.changePassword.ChangePassword;

public interface ChangePasswordRepository extends JpaRepository<ChangePassword, Long>{
	ChangePassword findByRecuperationEmailAndRecuperationCode(String recuperationEmail, String recuperationCode);
}
