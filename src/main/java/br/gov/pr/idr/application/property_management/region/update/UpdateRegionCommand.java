package br.gov.pr.idr.application.property_management.region.update;

import java.util.UUID;

public record UpdateRegionCommand(UUID id, String description) {
}
