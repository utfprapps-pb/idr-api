package br.gov.pr.idr.application.iam.user.create;

import br.gov.pr.idr.application.shared.CommandUseCase;
import br.gov.pr.idr.application.shared.UseCase;
import br.gov.pr.idr.domain.iam.user.User;
import br.gov.pr.idr.domain.iam.user.UserGateway;
import br.gov.pr.idr.domain.iam.user.vo.CPF;
import br.gov.pr.idr.domain.iam.user.vo.Password;
import br.gov.pr.idr.domain.property_management.city.CityID;
import br.gov.pr.idr.domain.shared.exceptions.NotificationException;
import br.gov.pr.idr.domain.shared.validation.NotificationValidation;

import java.util.Collections;

@CommandUseCase
public class CreateUserUseCase extends UseCase<CreateUserCommand, CreateUserOutput> {

    private final UserGateway userGateway;

    public CreateUserUseCase(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    @Override
    public CreateUserOutput execute(CreateUserCommand command) {
        final var cpf = CPF.from(command.cpf());
        final var userName = command.username();
        final var notification = NotificationValidation.create();

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

        user.validate(notification);

        if (notification.hasError()) {
            throw new NotificationException("Erro ao criar usuário", notification);
        }

        if (userGateway.existsByUsername(userName)) {
            throw new NotificationException("Username já cadastrado para outro usuário", notification);
        }

        if (userGateway.existsByCPF(cpf)) {
            throw new NotificationException("CPF já cadastrado para outro usuário", notification);
        }

        return CreateUserOutput.from(userGateway.create(user));
    }
}
