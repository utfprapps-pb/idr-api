package br.gov.pr.idr.application.iam.user.create;

import br.gov.pr.idr.application.shared.stereotype.CommandUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.iam.user.User;
import br.gov.pr.idr.domain.iam.user.UserGateway;
import br.gov.pr.idr.domain.iam.user.events.UserCreatedEvent;
import br.gov.pr.idr.domain.iam.user.vo.CPF;
import br.gov.pr.idr.domain.iam.user.vo.Password;
import br.gov.pr.idr.domain.property_management.city.CityGateway;
import br.gov.pr.idr.domain.property_management.city.CityID;
import br.gov.pr.idr.domain.shared.tactical.events.DomainEventPublisher;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotificationException;
import br.gov.pr.idr.domain.shared.tactical.validation.DomainError;
import br.gov.pr.idr.domain.shared.tactical.validation.NotificationValidation;

import java.util.Collections;

@CommandUseCase
public class CreateUserUseCase extends UseCase<CreateUserCommand, CreateUserOutput> {

    private final UserGateway userGateway;
    private final CityGateway cityGateway;
    private final DomainEventPublisher eventPublisher;

    public CreateUserUseCase(UserGateway userGateway,
                             CityGateway cityGateway,
                             DomainEventPublisher eventPublisher) {
        this.userGateway = userGateway;
        this.cityGateway = cityGateway;
        this.eventPublisher = eventPublisher;
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
            throw new NotificationException(notification.append(
                    DomainError.from("username", "Username já cadastrado para outro usuário")));
        }

        if (userGateway.existsByCPF(cpf)) {
            throw new NotificationException(notification.append(
                    DomainError.from("cpf", "CPF já cadastrado para outro usuário")));
        }

        if (!cityGateway.existsById(cityId)) {
            throw new NotificationException(
                    "Cidade com id %s não foi encontrada".formatted(command.cityId().toString()), notification);
        }

        final var createdUser = userGateway.create(user);
        eventPublisher.publish(new UserCreatedEvent(createdUser.getUsername(), createdUser.getName()));
        return CreateUserOutput.from(createdUser);
    }
}
