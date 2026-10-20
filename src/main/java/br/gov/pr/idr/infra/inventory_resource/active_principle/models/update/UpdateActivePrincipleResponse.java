package br.gov.pr.idr.infra.inventory_resource.active_principle.models.update;

import br.gov.pr.idr.application.inventory_resource.active_principle.update.UpdateActivePrincipleOutput;

import java.util.UUID;

public record UpdateActivePrincipleResponse(UUID id, String name) {

    public static UpdateActivePrincipleResponse from(final UpdateActivePrincipleOutput output) {
        return new UpdateActivePrincipleResponse(output.id(), output.name());
    }
}
