package br.edu.utfpr.ProjetoIDRAPI.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.edu.utfpr.ProjetoIDRAPI.validation.UserValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = UserValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUser {
	String message() default "Erro ao salvar registro! Valide as informações preenchidas e tente novamente.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
