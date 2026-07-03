package br.gov.pr.idr.application.property_management.property.retrieve.attachment;

import br.gov.pr.idr.application.shared.stereotype.QueryUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.property_management.property.Property;
import br.gov.pr.idr.domain.property_management.property.PropertyGateway;
import br.gov.pr.idr.domain.property_management.property.PropertyID;
import br.gov.pr.idr.domain.property_management.property.attachment.PropertyAttachmentID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;
import br.gov.pr.idr.domain.shared.storage.StorageGateway;

@QueryUseCase
public class GetPropertyAttachmentUseCase extends UseCase<GetPropertyAttachmentCommand, GetPropertyAttachmentOutput> {

    private final PropertyGateway propertyGateway;
    private final StorageGateway storageGateway;

    public GetPropertyAttachmentUseCase(final PropertyGateway propertyGateway, final StorageGateway storageGateway) {
        this.propertyGateway = propertyGateway;
        this.storageGateway = storageGateway;
    }

    @Override
    public GetPropertyAttachmentOutput execute(final GetPropertyAttachmentCommand command) {
        final var propertyId = PropertyID.from(command.propertyId());
        final var property = propertyGateway.findById(propertyId)
                .orElseThrow(() -> NotFoundException.with(Property.class, propertyId));

        final var attachmentId = PropertyAttachmentID.from(command.attachmentId());
        final var attachment = property.getAttachments().stream()
                .filter(a -> a.getId().equals(attachmentId))
                .findFirst()
                .orElseThrow(() -> NotFoundException.with("Anexo não encontrado para esta propriedade"));

        final var content = storageGateway.retrieve(attachment.getStorageKey());
        return GetPropertyAttachmentOutput.from(attachment, content);
    }
}
