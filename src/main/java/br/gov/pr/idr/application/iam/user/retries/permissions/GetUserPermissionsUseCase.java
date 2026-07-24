package br.gov.pr.idr.application.iam.user.retries.permissions;

import br.gov.pr.idr.application.shared.stereotype.QueryUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.iam.user.UserGateway;
import br.gov.pr.idr.domain.iam.user.UserID;
import br.gov.pr.idr.domain.iam.user.exceptions.UserException;

import java.util.UUID;

@QueryUseCase
public class GetUserPermissionsUseCase extends UseCase<UUID, GetUserPermissionsOutput> {

    private final UserGateway userGateway;

    public GetUserPermissionsUseCase(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    @Override
    public GetUserPermissionsOutput execute(final UUID id) {
        return userGateway.findById(UserID.from(id))
                .map(GetUserPermissionsOutput::from)
                .orElseThrow(() -> new UserException("Usuário com id %s não encontrado".formatted(id)));
    }
}
