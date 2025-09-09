package br.edu.utfpr.ProjetoIDRAPI.entity.changepassword;

public interface ChangePasswordService {
	String sendCode(String email);
	String getPasswordRecoveryCode(Long id);
	String changingUserPassword(ChangePassword change);
	String redirectButton(String code);
	ChangePassword findByEmailAndCode(String recuperationEmail, String recuperationCode);
}
