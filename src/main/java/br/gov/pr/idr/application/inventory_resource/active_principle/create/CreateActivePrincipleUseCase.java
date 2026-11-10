package br.gov.pr.idr.application.inventory_resource.active_principle.create;

import br.gov.pr.idr.application.shared.stereotype.CommandUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.inventory_resource.active_principle.ActivePrinciple;
import br.gov.pr.idr.domain.inventory_resource.active_principle.ActivePrincipleGateway;

@CommandUseCase
public class CreateActivePrincipleUseCase extends UseCase<CreateActivePrincipleCommand, CreateActivePrincipleOutput> {

    private final ActivePrincipleGateway activePrincipleGateway;

    public CreateActivePrincipleUseCase(ActivePrincipleGateway activePrincipleGateway) {
        this.activePrincipleGateway = activePrincipleGateway;
    }

    @Override
    public CreateActivePrincipleOutput execute(CreateActivePrincipleCommand command) {
        final var principle = ActivePrinciple.create(command.name());
        return CreateActivePrincipleOutput.from(activePrincipleGateway.save(principle));
    }
}
