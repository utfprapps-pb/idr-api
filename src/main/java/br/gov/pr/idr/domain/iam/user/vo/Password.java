package br.gov.pr.idr.domain.iam.user.vo;

import br.gov.pr.idr.domain.iam.user.exceptions.PasswordException;

import java.util.regex.Pattern;

public record Password(String pasword, String confirmPassword) {

    static final int MIN_LENGTH = 8;
    static final Pattern COMPLEXITY = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9]).+$");

    public Password {
        if (pasword == null || pasword.isBlank()) {
            throw new PasswordException("password", "Senha não pode ser nula ou vazia");
        }
        if (confirmPassword == null || confirmPassword.isBlank()) {
            throw new PasswordException("confirmPassword", "Confirmação de senha não pode ser nula ou vazia");
        }
        if (!pasword.equals(confirmPassword)) {
            throw new PasswordException("password", "Senhas não conferem");
        }
        if (pasword.length() < MIN_LENGTH) {
            throw new PasswordException("password", "Senha deve ter no mínimo %d caracteres".formatted(MIN_LENGTH));
        }
        if (!COMPLEXITY.matcher(pasword).matches()) {
            throw new PasswordException("password",
                    "Senha deve conter ao menos uma letra maiúscula, uma minúscula, um número e um caractere especial");
        }
    }

    public static Password from(final String pasword, final String confirmPassword) {
        return new Password(pasword, confirmPassword);
    }
}
