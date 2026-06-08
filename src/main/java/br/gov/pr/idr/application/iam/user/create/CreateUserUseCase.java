package br.gov.pr.idr.application.iam.user.create;

import br.gov.pr.idr.application.shared.CommandUseCase;
import br.gov.pr.idr.application.shared.UseCase;
import br.gov.pr.idr.domain.iam.user.User;
import br.gov.pr.idr.domain.iam.user.UserGateway;
import br.gov.pr.idr.domain.iam.user.vo.CPF;
import br.gov.pr.idr.domain.iam.user.vo.Password;
import br.gov.pr.idr.domain.property_management.city.CityGateway;
import br.gov.pr.idr.domain.property_management.city.CityID;
import br.gov.pr.idr.domain.shared.exceptions.NotificationException;
import br.gov.pr.idr.domain.shared.validation.NotificationValidation;

import java.util.Collections;

@CommandUseCase
public class CreateUserUseCase extends UseCase<CreateUserCommand, CreateUserOutput> {

    private final UserGateway userGateway;
    private final CityGateway cityGateway;

    public CreateUserUseCase(UserGateway userGateway,
                             CityGateway cityGateway) {
        this.userGateway = userGateway;
        this.cityGateway = cityGateway;
    }

    @Override
    public CreateUserOutput execute(CreateUserCommand command) {
        final var cityId = CityID.from(command.cityId());
        final var cpf = CPF.from(command.cpf());
        final var userName = command.username();

        final var user = User.create(
                command.name(),
                userName,
                Password.from(command.password(), command.confirmPassword()),
                cpf,
                command.phone(),
                CityID.from(command.cityId()),
                command.cep(),
                command.street(),
                command.houseNumber(),
                command.graduationYear(),
                command.professionalRegister(),
                Collections.emptySet()
        );

        final var notification = NotificationValidation.create();

        if (userGateway.existsByUsername(userName)) {
            throw new NotificationException("Username já cadastrado para outro usuário", notification);
        }

        if (userGateway.existsByCPF(cpf)) {
            throw new NotificationException("CPF já cadastrado para outro usuário", notification);
        }

        if (!cityGateway.existsById(cityId)) {
            throw new NotificationException(
                    "Cidade com id %s não foi encontrada".formatted(command.cityId().toString()), notification);
        }

        return CreateUserOutput.from(userGateway.create(user));
    }
}
