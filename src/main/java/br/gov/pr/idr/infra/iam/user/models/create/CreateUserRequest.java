package br.gov.pr.idr.infra.iam.user.models.create;

import java.util.UUID;

public record CreateUserRequest(
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
}
