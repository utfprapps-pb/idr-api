package br.edu.utfpr.ProjetoIDRAPI.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import br.edu.utfpr.ProjetoIDRAPI.annotation.UniqueUsername;
import br.edu.utfpr.ProjetoIDRAPI.repository.UserRepository;

public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

	@Autowired
	private UserRepository respository;
	
	@Override
	public boolean isValid(String username, ConstraintValidatorContext context) {
		if(respository.findByUsername(username) == null) {
			return true;
		}
		
		return false;
	}

}
