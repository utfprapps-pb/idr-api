package br.gov.pr.idr.infra.property_management.property.models.create;

import br.gov.pr.idr.application.property_management.property.create.CreatePropertyOutput;

import java.util.List;
import java.util.UUID;

public record CreatePropertyResponse(UUID id, String name, List<AttachmentResponse> attachments) {

    public record AttachmentResponse(UUID id, String fileName, String contentType, Long sizeBytes) {}

    public static CreatePropertyResponse from(final CreatePropertyOutput output) {
        return new CreatePropertyResponse(output.id(), output.name(),
                output.attachments().stream()
                        .map(a -> new AttachmentResponse(a.id(), a.fileName(), a.contentType(), a.sizeBytes()))
                        .toList());
    }
}
