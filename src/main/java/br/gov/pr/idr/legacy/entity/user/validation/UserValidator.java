package br.gov.pr.idr.legacy.entity.user.validation;

import br.gov.pr.idr.legacy.entity.user.annotation.ValidUser;
import br.gov.pr.idr.legacy.entity.user.User;
import br.gov.pr.idr.legacy.utils.BaseUser;
import org.springframework.beans.factory.annotation.Autowired;

import br.gov.pr.idr.legacy.entity.user.UserRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import java.util.Objects;

@Component
public class UserValidator implements ConstraintValidator<ValidUser, BaseUser> {

	@Autowired
	private UserRepository repository;

	public UserValidator() {
	}

	@Autowired
	public UserValidator(UserRepository repository) {
		this.repository = repository;
	}

	@Override
	public boolean isValid(BaseUser entity, ConstraintValidatorContext context) {
		if (repository == null) {
			SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		}

		if (entity == null || repository == null) {
			return true;
		}

		// Executa ambos para garantir que todas as mensagens de erro sejam adicionadas ao contexto
		boolean cpfValido = validateUniqueCPF(entity, context);
		boolean usernameValido = validateUniqueUsername(entity, context);

		return cpfValido && usernameValido;
	}

	boolean validateUniqueCPF(BaseUser entity, ConstraintValidatorContext context) {
		User user = repository.findByCpfAndCpfIsNotNull(entity.getCpf());
        boolean valid = user == null || Objects.equals(user.getId(), entity.getId());

		if (!valid) {
			handleMessage(context, "O CPF informado já está em uso", "cpf");
		}

		return valid;
    }

	boolean validateUniqueUsername(BaseUser entity, ConstraintValidatorContext context) {
		User user = repository.findByUsername(entity.getUsername());
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
