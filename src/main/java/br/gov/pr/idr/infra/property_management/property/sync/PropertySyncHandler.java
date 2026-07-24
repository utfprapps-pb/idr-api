package br.gov.pr.idr.infra.property_management.property.sync;

import br.gov.pr.idr.domain.iam.user.UserID;
import br.gov.pr.idr.domain.property_management.city.CityID;
import br.gov.pr.idr.domain.property_management.producer.ProducerGateway;
import br.gov.pr.idr.domain.property_management.producer.ProducerID;
import br.gov.pr.idr.domain.property_management.property.Property;
import br.gov.pr.idr.domain.property_management.property.PropertyGateway;
import br.gov.pr.idr.domain.property_management.property.attachment.PropertyAttachment;
import br.gov.pr.idr.domain.property_management.property.collaborator.PropertyCollaborator;
import br.gov.pr.idr.domain.property_management.property.vo.Coord;
import br.gov.pr.idr.domain.property_management.sync.context.SyncContext;
import br.gov.pr.idr.domain.property_management.sync.payload.SyncPayloadReader;
import br.gov.pr.idr.domain.property_management.sync.vo.OfflineEntityType;
import br.gov.pr.idr.domain.property_management.sync.entity.OfflineEntityCommand;
import br.gov.pr.idr.domain.property_management.sync.entity.SyncEntityHandler;
import br.gov.pr.idr.domain.property_management.sync.entity.SyncEntityResult;
import br.gov.pr.idr.domain.property_management.sync.vo.SyncEntityStatus;
import br.gov.pr.idr.domain.shared.storage.StorageGateway;
import br.gov.pr.idr.domain.shared.tactical.exceptions.UnprocessableEntityException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PropertySyncHandler implements SyncEntityHandler {

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "application/pdf", "image/png", "image/jpeg");
    private static final long MAX_FILE_SIZE_BYTES = 10L * 1024 * 1024;

    private final PropertyGateway propertyGateway;
    private final ProducerGateway producerGateway;
    private final StorageGateway storageGateway;

    @Override
    public OfflineEntityType type() {
        return OfflineEntityType.PROPERTY;
    }

    @Override
    public Set<OfflineEntityType> dependencies() {
        return Set.of(OfflineEntityType.PRODUCER);
    }

    @Override
    public SyncEntityResult handle(final OfflineEntityCommand command, final SyncContext context) {
        final var localId = command.localId();

        if (context.isAlreadySynced(localId)) {
            final var serverId = context.findPersistedServerId(localId).orElseThrow();
            context.registerLocalMapping(localId, serverId);
            return new SyncEntityResult(localId, serverId, SyncEntityStatus.EXISTING, null);
        }

        final var reader = SyncPayloadReader.of(command.data());
        final var producerServerId = resolveProducer(reader, context, localId);

        final var technicianIds = new java.util.LinkedHashSet<UserID>();
        reader.stringList("technicianIds").stream()
                .map(id -> UserID.from(UUID.fromString(id)))
                .forEach(technicianIds::add);
        technicianIds.add(context.technicianId());

        final var collaborators = reader.mapList("collaborators").stream()
                .map(item -> PropertyCollaborator.create(
                        SyncPayloadReader.stringValue(item, "name"),
                        SyncPayloadReader.stringValue(item, "hoursPerDay")))
                .toList();

        final var property = Property.create(
                reader.requiredString("name"),
                Coord.from(reader.bigDecimal("latitude"), reader.bigDecimal("longitude")),
                reader.bigDecimal("nakedAveragePrice"),
                reader.bigDecimal("leaseAveragePrice"),
                reader.doubleValue("dairyCattleFarming"),
                reader.doubleValue("perennialPasture"),
                reader.doubleValue("summerPlowing"),
                reader.doubleValue("winterPlowing"),
                ProducerID.from(producerServerId),
                CityID.from(reader.uuid("cityId")),
                java.util.List.copyOf(technicianIds),
                collaborators
        );

        attachFiles(property, reader.mapList("attachments"));

        final var saved = propertyGateway.save(property);
        final var serverId = saved.getId().id();
        context.persistMapping(localId, serverId, OfflineEntityType.PROPERTY);
        return new SyncEntityResult(localId, serverId, SyncEntityStatus.CREATED, null);
    }

    private void attachFiles(final Property property, final java.util.List<Map<String, Object>> attachments) {
        attachments.forEach(item -> {
            final var fileName = SyncPayloadReader.stringValue(item, "fileName");
            final var contentType = SyncPayloadReader.stringValue(item, "contentType");
            final var content = SyncPayloadReader.base64Value(item, "base64");
            validateAttachment(contentType, content.length);
            final var attachment = PropertyAttachment.create(
                    property.getId(), fileName, contentType, content.length);
            storageGateway.store(attachment.getStorageKey(), contentType, content);
            property.addAttachment(attachment);
        });
    }

    private void validateAttachment(final String contentType, final long sizeBytes) {
        if (!ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new UnprocessableEntityException("Tipo de arquivo não suportado: " + contentType);
        }
        if (sizeBytes > MAX_FILE_SIZE_BYTES) {
            throw new UnprocessableEntityException("Arquivo excede o tamanho máximo permitido de 10MB");
        }
    }

    private UUID resolveProducer(final SyncPayloadReader reader, final SyncContext context, final UUID propertyLocalId) {
        final var producerLocalId = reader.uuid("producerLocalId");
        if (producerLocalId != null) {
            final var fromBatch = context.resolveLocalMapping(producerLocalId);
            if (fromBatch.isPresent()) return fromBatch.get();
            final var persisted = context.findPersistedServerId(producerLocalId);
            if (persisted.isPresent()) return persisted.get();
        }

        final var directProducerId = reader.uuid("producerId");
        if (directProducerId != null && producerGateway.existsById(ProducerID.from(directProducerId))) {
            return directProducerId;
        }

        throw new UnprocessableEntityException(
                "PROPERTY com localId=" + propertyLocalId + " referencia producerLocalId inválido ou não encontrado");
    }
}
