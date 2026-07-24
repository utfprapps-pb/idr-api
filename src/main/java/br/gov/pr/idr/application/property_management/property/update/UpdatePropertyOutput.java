package br.gov.pr.idr.application.property_management.property.update;

import br.gov.pr.idr.domain.property_management.property.Property;

import java.util.List;
import java.util.UUID;

public record UpdatePropertyOutput(UUID id, String name, List<AttachmentSummary> attachments) {

    public record AttachmentSummary(UUID id, String fileName, String contentType, Long sizeBytes) {}

    public static UpdatePropertyOutput from(final Property property) {
        return new UpdatePropertyOutput(property.getId().id(), property.getName(),
                property.getAttachments().stream()
                        .map(a -> new AttachmentSummary(a.getId().id(), a.getFileName(), a.getContentType(), a.getSizeBytes()))
                        .toList());
    }
}
