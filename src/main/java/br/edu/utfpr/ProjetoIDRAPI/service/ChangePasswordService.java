package br.edu.utfpr.ProjetoIDRAPI.service;

import br.edu.utfpr.ProjetoIDRAPI.model.ChangePassword;

public interface ChangePasswordService {
	String sendCode(String email);
	String getPasswordRecoveryCode(Long id);
	String changingUserPassword(ChangePassword change);
	ChangePassword findByEmailAndCode(String recuperationEmail, String recuperationCode);
}
