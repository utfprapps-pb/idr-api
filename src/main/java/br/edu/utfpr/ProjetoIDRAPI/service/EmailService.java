package br.edu.utfpr.ProjetoIDRAPI.service;

import br.edu.utfpr.ProjetoIDRAPI.model.Email;

public interface EmailService {
	Email sendEmail(Email email);
	Email findByEmailTo(String emailTo);
	void deleteById(long id);
}
