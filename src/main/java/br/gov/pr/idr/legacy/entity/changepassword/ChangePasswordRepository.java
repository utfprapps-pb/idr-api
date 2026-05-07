package br.gov.pr.idr.legacy.entity.changepassword;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChangePasswordRepository extends JpaRepository<ChangePassword, Long>{
	ChangePassword findByRecuperationEmailAndRecuperationCode(String recuperationEmail, String recuperationCode);
}
