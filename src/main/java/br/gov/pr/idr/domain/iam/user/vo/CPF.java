package br.gov.pr.idr.domain.iam.user.vo;

import br.gov.pr.idr.domain.iam.user.exceptions.CPFException;
import br.gov.pr.idr.domain.shared.tactical.ValueObject;

@ValueObject
public record CPF(String value) {

    public CPF {
        if (value == null || value.isBlank()) {
            throw new CPFException("CPF não pode ser nulo ou vazio!");
        }
        final String digits = value.replaceAll("[.\\-]", "");
        if (!isValid(digits)) {
            throw new CPFException("CPF inválido!");
        }
    }

    public static CPF from(final String cpf) {
        return new CPF(cpf);
    }

    private static boolean isValid(String digits) {
        if (digits.length() != 11 || digits.matches("(\\d)\\1{10}")) {
            return false;
        }

        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += (digits.charAt(i) - '0') * (10 - i);
        }
        int first = 11 - (sum % 11);
        if (first >= 10) first = 0;
        if (first != (digits.charAt(9) - '0')) return false;

        sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += (digits.charAt(i) - '0') * (11 - i);
        }
        int second = 11 - (sum % 11);
        if (second >= 10) second = 0;
        return second == (digits.charAt(10) - '0');
    }
}
