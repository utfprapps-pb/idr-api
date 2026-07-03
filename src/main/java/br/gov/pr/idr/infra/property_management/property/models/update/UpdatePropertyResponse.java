package br.gov.pr.idr.infra.property_management.property.models.update;

import br.gov.pr.idr.application.property_management.property.update.UpdatePropertyOutput;

import java.util.List;
import java.util.UUID;

public record UpdatePropertyResponse(UUID id, String name, List<AttachmentResponse> attachments) {

    public record AttachmentResponse(UUID id, String fileName, String contentType, Long sizeBytes) {}

    public static UpdatePropertyResponse from(final UpdatePropertyOutput output) {
        return new UpdatePropertyResponse(output.id(), output.name(),
                output.attachments().stream()
                        .map(a -> new AttachmentResponse(a.id(), a.fileName(), a.contentType(), a.sizeBytes()))
                        .toList());
    }
}
