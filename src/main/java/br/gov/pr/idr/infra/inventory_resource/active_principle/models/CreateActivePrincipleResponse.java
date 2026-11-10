package br.gov.pr.idr.infra.inventory_resource.active_principle.models;

import br.gov.pr.idr.application.inventory_resource.active_principle.create.CreateActivePrincipleOutput;

import java.util.UUID;

public record CreateActivePrincipleResponse(UUID id, String name) {

    public static CreateActivePrincipleResponse from(final CreateActivePrincipleOutput output) {
        return new CreateActivePrincipleResponse(output.id(), output.name());
    }
}
