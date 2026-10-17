package br.gov.pr.idr.application.inventory_resource.active_principle.update;

import br.gov.pr.idr.domain.inventory_resource.active_principle.ActivePrinciple;

import java.util.UUID;

public record UpdateActivePrincipleOutput(UUID id, String name) {

    public static UpdateActivePrincipleOutput from(final ActivePrinciple entity) {
        return new UpdateActivePrincipleOutput(entity.getId().id(), entity.getName());
    }
}
