package br.gov.pr.idr.infra.property_management.property.persistence.attachment;

import br.gov.pr.idr.domain.property_management.property.attachment.PropertyAttachment;
import br.gov.pr.idr.domain.property_management.property.attachment.PropertyAttachmentID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity(name = "PropertyAttachment")
@Table(name = "property_attachment")
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PropertyAttachmentJPAEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "content_type", nullable = false)
    private String contentType;

    @Column(name = "size_bytes", nullable = false)
    private long sizeBytes;

    @Column(name = "storage_key", nullable = false)
    private String storageKey;

    public static PropertyAttachmentJPAEntity fromDomain(final PropertyAttachment attachment) {
        return new PropertyAttachmentJPAEntity(
                attachment.getId().id(),
                attachment.getFileName(),
                attachment.getContentType(),
                attachment.getSizeBytes(),
                attachment.getStorageKey()
        );
    }

    public PropertyAttachment toDomain() {
        return PropertyAttachment.with(
                PropertyAttachmentID.from(this.id),
                this.fileName,
                this.contentType,
                this.sizeBytes,
                this.storageKey
        );
    }
}
