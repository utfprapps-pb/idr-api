package br.gov.pr.idr.domain.property_management.property.attachment;

import br.gov.pr.idr.domain.property_management.property.PropertyID;
import br.gov.pr.idr.domain.shared.tactical.Entity;
import br.gov.pr.idr.domain.shared.tactical.validation.DomainError;
import br.gov.pr.idr.domain.shared.tactical.validation.ValidationHandler;

public class PropertyAttachment extends Entity<PropertyAttachmentID> {

    private final String fileName;
    private final String contentType;
    private final long sizeBytes;
    private final String storageKey;

    protected PropertyAttachment(final PropertyAttachmentID id,
                                 final String fileName,
                                 final String contentType,
                                 final long sizeBytes,
                                 final String storageKey
    ) {
        super(id);
        this.fileName = fileName;
        this.contentType = contentType;
        this.sizeBytes = sizeBytes;
        this.storageKey = storageKey;
    }

    public static PropertyAttachment create(final PropertyID propertyId,
                                            final String fileName,
                                            final String contentType,
                                            final long sizeBytes
    ) {
        final var id = PropertyAttachmentID.unique();
        final var storageKey = "properties/%s/%s/%s".formatted(propertyId.id(), id.id(), fileName);
        final var attachment = new PropertyAttachment(id, fileName, contentType, sizeBytes, storageKey);
        attachment.selfValidate();
        return attachment;
    }

    public static PropertyAttachment with(final PropertyAttachmentID id,
                                          final String fileName,
                                          final String contentType,
                                          final long sizeBytes,
                                          final String storageKey
    ) {
        return new PropertyAttachment(id, fileName, contentType, sizeBytes, storageKey);
    }

    @Override
    public void validate(ValidationHandler handler) {
        if (this.fileName == null || this.fileName.isBlank()) {
            handler.append(DomainError.from("Nome do arquivo do anexo é obrigatório"));
        }
        if (this.contentType == null || this.contentType.isBlank()) {
            handler.append(DomainError.from("Tipo de conteúdo do anexo é obrigatório"));
        }
        if (this.sizeBytes <= 0) {
            handler.append(DomainError.from("Tamanho do anexo deve ser maior que zero"));
        }
        if (this.storageKey == null || this.storageKey.isBlank()) {
            handler.append(DomainError.from("Chave de armazenamento do anexo é obrigatória"));
        }
    }

    public String getFileName() {
        return fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public long getSizeBytes() {
        return sizeBytes;
    }

    public String getStorageKey() {
        return storageKey;
    }
}
