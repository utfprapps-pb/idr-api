package br.gov.pr.idr.application.property_management.property.retrieve.attachment;

import java.util.UUID;

public record GetPropertyAttachmentCommand(UUID propertyId, UUID attachmentId) {

    public static GetPropertyAttachmentCommand from(final UUID propertyId, final UUID attachmentId) {
        return new GetPropertyAttachmentCommand(propertyId, attachmentId);
    }
}
