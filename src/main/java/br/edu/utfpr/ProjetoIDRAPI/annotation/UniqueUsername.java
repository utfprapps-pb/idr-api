package br.edu.utfpr.ProjetoIDRAPI.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import br.edu.utfpr.ProjetoIDRAPI.validation.UniqueUsernameValidator;

@Constraint(validatedBy = UniqueUsernameValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueUsername {
	String message() default "Erro ao salvar registro! O nome de usuário informado já existe!";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
