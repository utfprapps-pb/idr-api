package br.gov.pr.idr.application.iam.user.create;

import java.util.UUID;

public record CreateUserCommand(
        String name,
        String username,
        String password,
        String confirmPassword,
        String cpf,
        String phone,
        String graduationYear,
        String professionalRegister,
        String cep,
        String street,
        UUID cityId,
        String houseNumber
) {

    public static CreateUserCommand from(final String name,
                                         final String username,
                                         final String password,
                                         final String confirmPassword,
                                         final String cpf,
                                         final String phone,
                                         final String graduationYear,
                                         final String professionalRegister,
                                         final String cep,
                                         final String street,
                                         final UUID cityId,
                                         final String houseNumber) {
        return new CreateUserCommand(name, username, password, confirmPassword, cpf, phone, graduationYear, professionalRegister,
                cep, street, cityId, houseNumber);
    }
}
