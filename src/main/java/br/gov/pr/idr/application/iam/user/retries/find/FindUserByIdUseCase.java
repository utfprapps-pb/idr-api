package br.gov.pr.idr.application.iam.user.retries.find;

import br.gov.pr.idr.application.shared.QueryUseCase;
import br.gov.pr.idr.application.shared.UseCase;
import br.gov.pr.idr.domain.iam.user.UserGateway;
import br.gov.pr.idr.domain.iam.user.UserID;
import br.gov.pr.idr.domain.iam.user.exceptions.UserException;

import java.util.UUID;

@QueryUseCase
public class FindUserByIdUseCase extends UseCase<UUID, FindUserByIdOutput> {

    private final UserGateway userGateway;

    public FindUserByIdUseCase(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    @Override
    public FindUserByIdOutput execute(final UUID id) {
        return userGateway.findById(UserID.from(id))
                .map(FindUserByIdOutput::from)
                .orElseThrow(() -> new UserException("Usuário com id %s não encontrado".formatted(id)));
    }
}
