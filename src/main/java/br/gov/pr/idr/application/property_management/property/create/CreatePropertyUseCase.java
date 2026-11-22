package br.gov.pr.idr.application.property_management.property.create;

import br.gov.pr.idr.application.shared.stereotype.CommandUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.iam.user.UserID;
import br.gov.pr.idr.domain.property_management.city.CityGateway;
import br.gov.pr.idr.domain.property_management.city.CityID;
import br.gov.pr.idr.domain.property_management.producer.ProducerGateway;
import br.gov.pr.idr.domain.property_management.producer.ProducerID;
import br.gov.pr.idr.domain.property_management.property.Property;
import br.gov.pr.idr.domain.property_management.property.PropertyGateway;
import br.gov.pr.idr.domain.property_management.property.attachment.PropertyAttachment;
import br.gov.pr.idr.domain.property_management.property.collaborator.PropertyCollaborator;
import br.gov.pr.idr.domain.property_management.property.vo.Coord;
import br.gov.pr.idr.domain.shared.tactical.validation.NotificationValidation;
import br.gov.pr.idr.domain.shared.tactical.validation.DomainError;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotificationException;
import br.gov.pr.idr.domain.shared.tactical.exceptions.UnprocessableEntityException;
import br.gov.pr.idr.domain.shared.storage.StorageGateway;

import java.util.List;
import java.util.Set;

@CommandUseCase
public class CreatePropertyUseCase extends UseCase<CreatePropertyCommand, CreatePropertyOutput> {

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "application/pdf", "image/png", "image/jpeg");
    private static final long MAX_FILE_SIZE_BYTES = 10L * 1024 * 1024;

    private final PropertyGateway propertyGateway;
    private final CityGateway cityGateway;
    private final ProducerGateway producerGateway;
    private final StorageGateway storageGateway;

    public CreatePropertyUseCase(final PropertyGateway propertyGateway,
                                 final CityGateway cityGateway,
                                 final ProducerGateway producerGateway,
                                 final StorageGateway storageGateway) {
        this.propertyGateway = propertyGateway;
        this.cityGateway = cityGateway;
        this.producerGateway = producerGateway;
        this.storageGateway = storageGateway;
    }

    @Override
    public CreatePropertyOutput execute(CreatePropertyCommand command) {
        final var producerId = ProducerID.from(command.producerId());
        final var cityId = CityID.from(command.cityId());
        final var coord = Coord.from(command.latitude(), command.longitude());

        if (!cityGateway.existsById(cityId)) {
            throw referenceNotFound("cityId", "Cidade não encontrada");
        }

        if (!producerGateway.existsById(producerId)) {
            throw referenceNotFound("producerId", "Produtor não encontrado");
        }

        final var technicianIds = command
                .technicianIds()
                .stream()
                .map(UserID::from)
                .toList();
        final var collaborators = command
                .collaborators()
                .stream()
                .map(c -> PropertyCollaborator.create(c.name(), c.hoursPerDay()))
                .toList();

        final var property = Property.create(command.name(), coord, command.nakedAveragePrice(),
                                             command.leaseAveragePrice(), command.dairyCattleFarming(),
                                             command.perennialPasture(), command.summerPlowing(),
                                             command.winterPlowing(), producerId, cityId, technicianIds, collaborators);

        this.attachFiles(property, command.attachments());

        return CreatePropertyOutput.from(propertyGateway.save(property));
    }

    private void attachFiles(final Property property, final List<CreatePropertyCommand.AttachmentData> attachments) {
        attachments.forEach(data -> validateAttachment(data.contentType(), data.content().length));
        attachments.forEach(data -> {
            final var attachment = PropertyAttachment.create(
                    property.getId(), data.fileName(), data.contentType(), data.content().length);
            storageGateway.store(attachment.getStorageKey(), data.contentType(), data.content());
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

    /** Id do corpo da requisição que não existe: erro de validação (400) no campo, não 404 da rota. */
    private static NotificationException referenceNotFound(final String field, final String message) {
        final var validation = NotificationValidation.create();
        validation.append(DomainError.from(field, message));
        return new NotificationException(message, validation);
    }
}
