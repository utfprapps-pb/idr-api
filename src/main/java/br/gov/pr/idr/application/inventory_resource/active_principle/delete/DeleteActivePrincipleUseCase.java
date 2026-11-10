package br.gov.pr.idr.application.inventory_resource.active_principle.delete;

import br.gov.pr.idr.application.shared.stereotype.CommandUseCase;
import br.gov.pr.idr.application.shared.stereotype.VoidUseCase;
import br.gov.pr.idr.domain.inventory_resource.active_principle.ActivePrinciple;
import br.gov.pr.idr.domain.inventory_resource.active_principle.ActivePrincipleGateway;
import br.gov.pr.idr.domain.inventory_resource.active_principle.ActivePrincipleID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;

import java.util.UUID;

@CommandUseCase
public class DeleteActivePrincipleUseCase extends VoidUseCase<UUID> {

    private final ActivePrincipleGateway activePrincipleGateway;

    public DeleteActivePrincipleUseCase(ActivePrincipleGateway activePrincipleGateway) {
        this.activePrincipleGateway = activePrincipleGateway;
    }

    @Override
    public void execute(final UUID id) {
        final var principleId = ActivePrincipleID.from(id);
        if (!activePrincipleGateway.existsById(principleId)) {
            throw NotFoundException.with(ActivePrinciple.class, principleId);
        }
        activePrincipleGateway.deleteById(principleId);
    }
}
