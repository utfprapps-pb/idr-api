package br.gov.pr.idr.application.property_management.property.update;

import br.gov.pr.idr.application.shared.stereotype.CommandUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.iam.user.UserID;
import br.gov.pr.idr.domain.property_management.city.City;
import br.gov.pr.idr.domain.property_management.city.CityGateway;
import br.gov.pr.idr.domain.property_management.city.CityID;
import br.gov.pr.idr.domain.property_management.producer.Producer;
import br.gov.pr.idr.domain.property_management.producer.ProducerGateway;
import br.gov.pr.idr.domain.property_management.producer.ProducerID;
import br.gov.pr.idr.domain.property_management.property.Property;
import br.gov.pr.idr.domain.property_management.property.PropertyGateway;
import br.gov.pr.idr.domain.property_management.property.PropertyID;
import br.gov.pr.idr.domain.property_management.property.attachment.PropertyAttachment;
import br.gov.pr.idr.domain.property_management.property.attachment.PropertyAttachmentID;
import br.gov.pr.idr.domain.property_management.property.collaborator.PropertyCollaborator;
import br.gov.pr.idr.domain.property_management.property.vo.Coord;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;
import br.gov.pr.idr.domain.shared.storage.StorageGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

@CommandUseCase
public class UpdatePropertyUseCase extends UseCase<UpdatePropertyCommand, UpdatePropertyOutput> {

    private static final Logger log = LoggerFactory.getLogger(UpdatePropertyUseCase.class);
    private final PropertyGateway propertyGateway;
    private final CityGateway cityGateway;
    private final ProducerGateway producerGateway;
    private final StorageGateway storageGateway;

    public UpdatePropertyUseCase(final PropertyGateway propertyGateway, final CityGateway cityGateway,
                                 final ProducerGateway producerGateway, final StorageGateway storageGateway) {
        this.propertyGateway = propertyGateway;
        this.cityGateway = cityGateway;
        this.producerGateway = producerGateway;
        this.storageGateway = storageGateway;
    }

    @Override
    public UpdatePropertyOutput execute(final UpdatePropertyCommand command) {
        final var propertyId = PropertyID.from(command.id());
        final var cityId = CityID.from(command.cityId());
        final var producerId = ProducerID.from(command.producerId());

        final var property = propertyGateway.findById(propertyId)
                .orElseThrow(() -> NotFoundException.with(Property.class, propertyId));

        if (!cityGateway.existsById(cityId)) {
            throw NotFoundException.with(City.class, cityId);
        }

        if (!producerGateway.existsById(producerId)) {
            throw NotFoundException.with(Producer.class, producerId);
        }

        final var coord = Coord.from(command.latitude(), command.longitude());
        final var technicianIds = command.technicianIds().stream().map(UserID::from).toList();
        final var collaborators = command.collaborators().stream()
                .map(c -> PropertyCollaborator.create(c.name(), c.hoursPerDay()))
                .toList();

        property.update(command.name(), coord, command.nakedAveragePrice(), command.leaseAveragePrice(),
                command.dairyCattleFarming(), command.perennialPasture(), command.summerPlowing(),
                command.winterPlowing(), producerId, cityId, technicianIds, collaborators);

        this.attachFiles(property, command.attachments());
        final var removedStorageKeys = this.detachFiles(property, command.removeAttachmentIds());

        final var updated = propertyGateway.update(property);
        this.deleteRemovedFiles(removedStorageKeys);
        return UpdatePropertyOutput.from(updated);
    }

    private void attachFiles(final Property property, final List<UpdatePropertyCommand.AttachmentData> attachments) {
        attachments.forEach(data -> {
            final var attachment = PropertyAttachment.create(
                    property.getId(), data.fileName(), data.contentType(), data.content().length);
            storageGateway.store(attachment.getStorageKey(), data.contentType(), data.content());
            property.addAttachment(attachment);
        });
    }

    private List<String> detachFiles(final Property property, final List<UUID> removeAttachmentIds) {
        return removeAttachmentIds.stream().map(id -> {
            final var attachmentId = PropertyAttachmentID.from(id);
            final var storageKey = property.getAttachments().stream()
                    .filter(attachment -> attachment.getId().equals(attachmentId))
                    .findFirst()
                    .map(PropertyAttachment::getStorageKey)
                    .orElse(null);
            property.removeAttachment(attachmentId);
            return storageKey;
        }).toList();
    }

    private void deleteRemovedFiles(final List<String> storageKeys) {
        storageKeys.forEach(key -> {
            try {
                storageGateway.delete(key);
            } catch (Exception e) {
                log.warn("Falha ao remover arquivo do storage para a chave {}: {}", key, e.getMessage());
            }
        });
    }
}
