package br.gov.pr.idr.application.inventory_resource.active_principle.update;

import br.gov.pr.idr.application.shared.stereotype.CommandUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.inventory_resource.active_principle.ActivePrincipleGateway;
import br.gov.pr.idr.domain.inventory_resource.active_principle.ActivePrincipleID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;

@CommandUseCase
public class UpdateActivePrincipleUseCase extends UseCase<UpdateActivePrincipleCommand, UpdateActivePrincipleOutput> {

    private final ActivePrincipleGateway activePrincipleGateway;

    public UpdateActivePrincipleUseCase(ActivePrincipleGateway activePrincipleGateway) {
        this.activePrincipleGateway = activePrincipleGateway;
    }

    @Override
    public UpdateActivePrincipleOutput execute(UpdateActivePrincipleCommand command) {
        final var id = ActivePrincipleID.from(command.id());
        final var name = command.name();

        final var principle = activePrincipleGateway.findById(id)
                .orElseThrow(() -> NotFoundException.with(
                        "ActivePrinciple com o ID %s não encontrada".formatted(id.id())))
                .update(name);

        return UpdateActivePrincipleOutput.from(activePrincipleGateway.update(principle));
    }
}
