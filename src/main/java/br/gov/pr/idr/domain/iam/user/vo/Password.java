package br.gov.pr.idr.domain.iam.user.vo;

import br.gov.pr.idr.domain.iam.user.exceptions.PasswordException;

import java.util.regex.Pattern;

public record Password(String pasword, String confirmPassword) {

    //TODO
    static final Pattern pattern = Pattern.compile("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])");

    public Password {

        if (pasword == null || pasword.isBlank()) {
            throw new PasswordException("Senha não pode ser nula ou vazia");
        }
        if (confirmPassword == null || confirmPassword.isBlank()) {
            throw new PasswordException("Confirmação de senha não pode ser nula ou vazia");
        }
        if (!pasword.equals(confirmPassword)) {
            throw new PasswordException("Senhas não conferem");
        }
//        if (!pattern.matcher(pasword).matches()) {
//            throw new PasswordException("A senha deve conter pelo menos um número, uma letra minúscula e uma letra maiúscula");
//        }
    }

    public static Password from(final String pasword, final String confirmPassword) {
        return new Password(pasword, confirmPassword);
    }
}
