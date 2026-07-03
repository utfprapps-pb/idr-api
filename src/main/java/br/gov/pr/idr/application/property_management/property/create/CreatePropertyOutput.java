package br.gov.pr.idr.application.property_management.property.create;

import br.gov.pr.idr.domain.property_management.property.Property;

import java.util.List;
import java.util.UUID;

public record CreatePropertyOutput(UUID id, String name, List<AttachmentSummary> attachments) {

    public record AttachmentSummary(UUID id, String fileName, String contentType, Long sizeBytes) {}

    public static CreatePropertyOutput from(final Property entity) {
        return new CreatePropertyOutput(entity.getId().id(), entity.getName(),
                entity.getAttachments().stream()
                        .map(a -> new AttachmentSummary(a.getId().id(), a.getFileName(), a.getContentType(), a.getSizeBytes()))
                        .toList());
    }
}
