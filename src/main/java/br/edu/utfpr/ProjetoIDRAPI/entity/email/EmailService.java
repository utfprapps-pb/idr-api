package br.edu.utfpr.ProjetoIDRAPI.entity.email;

import br.edu.utfpr.ProjetoIDRAPI.entity.email.Email;

public interface EmailService {
	Email sendEmail(Email email);
	Email findByEmailTo(String emailTo);
	void deleteById(long id);
}
