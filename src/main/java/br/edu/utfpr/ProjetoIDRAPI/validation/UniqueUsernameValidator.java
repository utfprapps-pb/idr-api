package br.edu.utfpr.ProjetoIDRAPI.validation;

import org.springframework.beans.factory.annotation.Autowired;

import br.edu.utfpr.ProjetoIDRAPI.annotation.UniqueUsername;
import br.edu.utfpr.ProjetoIDRAPI.repository.UserRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

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
