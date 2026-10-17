package br.gov.pr.idr.application.inventory_resource.active_principle.create;

import br.gov.pr.idr.domain.inventory_resource.active_principle.ActivePrinciple;

import java.util.UUID;

public record CreateActivePrincipleOutput(UUID id, String name) {

    public static CreateActivePrincipleOutput from(final ActivePrinciple entity) {
        return new CreateActivePrincipleOutput(entity.getId().id(), entity.getName());
    }
}
