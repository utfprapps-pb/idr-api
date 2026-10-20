package br.gov.pr.idr.application.inventory_resource.active_principle.retrieve.list;

import br.gov.pr.idr.domain.inventory_resource.active_principle.ActivePrinciple;

import java.util.UUID;

public record ListActivePrincipleOutput(UUID id, String name) {

    public static ListActivePrincipleOutput from(final ActivePrinciple entity) {
        return new ListActivePrincipleOutput(entity.getId().id(), entity.getName());
    }
}
