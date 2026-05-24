package br.gov.pr.idr.domain.iam.user.vo;

import br.gov.pr.idr.domain.iam.user.exceptions.CPFException;
import br.gov.pr.idr.domain.shared.ValueObject;

import java.util.regex.Pattern;

@ValueObject
public record CPF(String value) {

    static Pattern pattern = Pattern.compile("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}");

    public CPF {
        if (value == null || value.isBlank()) {
            throw new CPFException("CPF não pode ser nulo ou vazio!");
        }
//        if (!pattern.matcher(value).matches()) {
//            throw new CPFException("CPF inválido!");
//        }

    }

    public static CPF from(final String cpf) {
        return new CPF(cpf);
    }
}
