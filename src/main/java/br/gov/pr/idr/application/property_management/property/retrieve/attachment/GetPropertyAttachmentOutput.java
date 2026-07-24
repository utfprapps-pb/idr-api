package br.gov.pr.idr.application.property_management.property.retrieve.attachment;

import br.gov.pr.idr.domain.property_management.property.attachment.PropertyAttachment;

public record GetPropertyAttachmentOutput(String fileName, String contentType, byte[] content) {

    public static GetPropertyAttachmentOutput from(final PropertyAttachment attachment, final byte[] content) {
        return new GetPropertyAttachmentOutput(attachment.getFileName(), attachment.getContentType(), content);
    }
}
