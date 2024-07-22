package br.edu.utfpr.ProjetoIDRAPI.entity.user.validation;

import br.edu.utfpr.ProjetoIDRAPI.entity.user.annotation.ValidUser;
import br.edu.utfpr.ProjetoIDRAPI.entity.user.User;
import br.edu.utfpr.ProjetoIDRAPI.utils.BaseUser;
import org.springframework.beans.factory.annotation.Autowired;

import br.edu.utfpr.ProjetoIDRAPI.entity.user.UserRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

public class UserValidator implements ConstraintValidator<ValidUser, BaseUser> {

	@Autowired
	private UserRepository respository;
	
	@Override
	public boolean isValid(BaseUser entity, ConstraintValidatorContext context) {
		return validateUniqueCPF(entity, context) && validateUniqueUsername(entity, context);
	}

	boolean validateUniqueCPF(BaseUser entity, ConstraintValidatorContext context) {
		User user = respository.findByCpf(entity.getCpf());
        boolean valid = user == null || Objects.equals(user.getId(), entity.getId());

		if (!valid) {
			handleMessage(context, "O CPF informado já está em uso", "cpf");
		}

		return valid;
    }

	boolean validateUniqueUsername(BaseUser entity, ConstraintValidatorContext context) {
		User user = respository.findByUsername(entity.getUsername());
		boolean valid = user == null || Objects.equals(user.getId(), entity.getId());

		if (!valid) {
			handleMessage(context, "O nome de usuário informado já está em uso", "username");
		}

		return valid;
	}

	public void handleMessage(ConstraintValidatorContext context, String message, String node) {
		context.buildConstraintViolationWithTemplate(message)
				.addPropertyNode(node)
				.addConstraintViolation()
				.disableDefaultConstraintViolation();
	}

}
