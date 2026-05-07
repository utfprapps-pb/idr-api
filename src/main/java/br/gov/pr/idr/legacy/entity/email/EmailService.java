package br.gov.pr.idr.legacy.entity.email;

public interface EmailService {
	Email sendEmail(Email email);
	Email findByEmailTo(String emailTo);
	void deleteById(long id);
}
