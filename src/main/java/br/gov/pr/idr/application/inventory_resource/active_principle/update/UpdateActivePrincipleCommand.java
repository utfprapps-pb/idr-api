package br.gov.pr.idr.application.inventory_resource.active_principle.update;

import java.util.UUID;

public record UpdateActivePrincipleCommand(UUID id, String name) {
}
