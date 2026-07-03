package br.gov.pr.idr.application.iam.user.update;

import br.gov.pr.idr.application.shared.stereotype.CommandUseCase;
import br.gov.pr.idr.application.shared.stereotype.VoidUseCase;
import br.gov.pr.idr.domain.iam.permission.Permission;
import br.gov.pr.idr.domain.iam.user.UserGateway;
import br.gov.pr.idr.domain.iam.user.UserID;
import br.gov.pr.idr.domain.iam.user.exceptions.UserException;

import java.util.Set;

@CommandUseCase
public class UpdateUserPermissionsUseCase extends VoidUseCase<UpdateUserPermissionsCommand> {

    private final UserGateway userGateway;

    public UpdateUserPermissionsUseCase(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    @Override
    public void execute(final UpdateUserPermissionsCommand command) {
        final var user = userGateway.findById(UserID.from(command.userId()))
                .orElseThrow(() -> new UserException("Usuário com id %s não encontrado".formatted(command.userId())));

        final var newPermission = Permission.create(
                command.role(),
                command.readOnly(),
                command.regionIds(),
                command.cityIds()
        );

        user.update(
                user.getPhone(),
                user.getCityID(),
                user.getCep(),
                user.getStreet(),
                user.getHouseNumber(),
                user.getProfessionalRegister(),
                user.getGraduationYear(),
                user.getPassword(),
                user.isActive(),
                Set.of(newPermission)
        );
        userGateway.update(user);
    }
}
