package br.gov.pr.idr.application.iam.user.update;

import br.gov.pr.idr.application.shared.stereotype.CommandUseCase;
import br.gov.pr.idr.application.shared.stereotype.VoidUseCase;
import br.gov.pr.idr.domain.iam.user.UserGateway;
import br.gov.pr.idr.domain.iam.user.UserID;
import br.gov.pr.idr.domain.iam.user.exceptions.UserException;

import java.util.UUID;

@CommandUseCase
public class ToggleUserActiveUseCase extends VoidUseCase<UUID> {

    private final UserGateway userGateway;

    public ToggleUserActiveUseCase(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    @Override
    public void execute(final UUID userId) {
        final var user = userGateway.findById(UserID.from(userId))
                .orElseThrow(() -> new UserException("Usuário com id %s não encontrado".formatted(userId)));
        user.update(
                user.getPhone(),
                user.getCityID(),
                user.getCep(),
                user.getStreet(),
                user.getHouseNumber(),
                user.getProfessionalRegister(),
                user.getGraduationYear(),
                user.getPassword(),
                !user.isActive(),
                user.getPermissions()
        );
        userGateway.update(user);
    }
}
