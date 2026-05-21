package br.gov.pr.idr.application.iam.user.retries;

import br.gov.pr.idr.application.shared.ManagedUseCase;
import br.gov.pr.idr.application.shared.UseCase;
import br.gov.pr.idr.domain.iam.user.UserGateway;
import br.gov.pr.idr.domain.iam.user.exceptions.UserException;

@ManagedUseCase
public class FindUserByUsernameUseCase extends UseCase<String, FindUserByUsernameOutput> {

    private final UserGateway userGateway;

    public FindUserByUsernameUseCase(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    @Override
    public FindUserByUsernameOutput execute(String command) {
        return userGateway.findByUsername(command).map(
                FindUserByUsernameOutput::from).orElseThrow(
                () -> new UserException("Usuário com o username %s não encontrado".formatted(command)));
    }
}
