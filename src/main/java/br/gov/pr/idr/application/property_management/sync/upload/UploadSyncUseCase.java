package br.gov.pr.idr.application.property_management.sync.upload;

import br.gov.pr.idr.application.shared.stereotype.CommandUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.iam.user.UserID;
import br.gov.pr.idr.domain.iam.user.vo.CPF;
import br.gov.pr.idr.domain.property_management.city.CityID;
import br.gov.pr.idr.domain.property_management.producer.Producer;
import br.gov.pr.idr.domain.property_management.producer.ProducerGateway;
import br.gov.pr.idr.domain.property_management.producer.ProducerID;
import br.gov.pr.idr.domain.property_management.property.Property;
import br.gov.pr.idr.domain.property_management.property.PropertyGateway;
import br.gov.pr.idr.domain.property_management.property.vo.Coord;
import br.gov.pr.idr.domain.property_management.sync.OfflineEntityType;
import br.gov.pr.idr.domain.property_management.sync.SyncEntityResult;
import br.gov.pr.idr.domain.property_management.sync.SyncEntityStatus;
import br.gov.pr.idr.domain.shared.tactical.exceptions.DomainException;
import br.gov.pr.idr.domain.shared.tactical.exceptions.UnprocessableEntityException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@CommandUseCase
public class UploadSyncUseCase extends UseCase<UploadSyncCommand, List<SyncEntityResult>> {

    private static final int MAX_BATCH_SIZE = 100;

    private final ProducerGateway producerGateway;
    private final PropertyGateway propertyGateway;

    public UploadSyncUseCase(final ProducerGateway producerGateway,
                             final PropertyGateway propertyGateway) {
        this.producerGateway = producerGateway;
        this.propertyGateway = propertyGateway;
    }

    @Override
    public List<SyncEntityResult> execute(final UploadSyncCommand command) {
        if (command.entities().size() > MAX_BATCH_SIZE) {
            throw DomainException.from("Batch não pode exceder " + MAX_BATCH_SIZE + " entidades");
        }

        final var producers = command.entities().stream()
                .filter(e -> e.type() == OfflineEntityType.PRODUCER)
                .toList();
        final var properties = command.entities().stream()
                .filter(e -> e.type() == OfflineEntityType.PROPERTY)
                .toList();

        final Map<UUID, UUID> localToServerIdMap = new HashMap<>();
        final List<SyncEntityResult> results = new ArrayList<>();

        for (final var entity : producers) {
            final var result = processProducer(entity, localToServerIdMap);
            results.add(result);
        }

        for (final var entity : properties) {
            final var result = processProperty(entity, localToServerIdMap);
            results.add(result);
        }

        return results;
    }

    private SyncEntityResult processProducer(final UploadSyncCommand.OfflineEntityCommand entity,
                                              final Map<UUID, UUID> localToServerIdMap) {
        final var cpfStr = (String) entity.data().get("cpf");
        final var cpf = CPF.from(cpfStr);

        if (producerGateway.existsByCpf(cpf)) {
            final var existing = producerGateway.findByCpf(cpf)
                    .orElseThrow(() -> new UnprocessableEntityException("Produtor com CPF " + cpfStr + " não encontrado"));
            localToServerIdMap.put(entity.localId(), existing.getId().id());
            return new SyncEntityResult(entity.localId(), existing.getId().id(), SyncEntityStatus.EXISTING, null);
        }

        final var name = (String) entity.data().get("name");
        final var saved = producerGateway.save(Producer.create(name, cpf));
        localToServerIdMap.put(entity.localId(), saved.getId().id());
        return new SyncEntityResult(entity.localId(), saved.getId().id(), SyncEntityStatus.CREATED, null);
    }

    private SyncEntityResult processProperty(final UploadSyncCommand.OfflineEntityCommand entity,
                                              final Map<UUID, UUID> localToServerIdMap) {
        final var producerLocalId = toUUID(entity.data().get("producerLocalId"));
        final UUID producerServerId;

        if (producerLocalId != null && localToServerIdMap.containsKey(producerLocalId)) {
            producerServerId = localToServerIdMap.get(producerLocalId);
        } else {
            final var directProducerId = toUUID(entity.data().get("producerId"));
            if (directProducerId != null && producerGateway.existsById(ProducerID.from(directProducerId))) {
                producerServerId = directProducerId;
            } else {
                throw new UnprocessableEntityException(
                        "PROPERTY com localId=" + entity.localId() + " referencia producerLocalId inválido ou não encontrado");
            }
        }

        final var name = (String) entity.data().get("name");
        final var cityId = CityID.from(toUUID(entity.data().get("cityId")));
        final var latitude = toBigDecimal(entity.data().get("latitude"));
        final var longitude = toBigDecimal(entity.data().get("longitude"));
        final var nakedAvgPrice = toBigDecimal(entity.data().get("nakedAveragePrice"));
        final var leaseAvgPrice = toBigDecimal(entity.data().get("leaseAveragePrice"));
        final var dairyCattle = toDouble(entity.data().get("dairyCattleFarming"));
        final var perennialPasture = toDouble(entity.data().get("perennialPasture"));
        final var summerPlowing = toDouble(entity.data().get("summerPlowing"));
        final var winterPlowing = toDouble(entity.data().get("winterPlowing"));

        @SuppressWarnings("unchecked")
        final var technicianIdsList = (List<String>) entity.data().getOrDefault("technicianIds", List.of());
        final var technicianIds = technicianIdsList.stream()
                .map(id -> UserID.from(UUID.fromString(id)))
                .toList();

        final var property = Property.create(
                name,
                Coord.from(latitude, longitude),
                nakedAvgPrice,
                leaseAvgPrice,
                dairyCattle,
                perennialPasture,
                summerPlowing,
                winterPlowing,
                ProducerID.from(producerServerId),
                cityId,
                technicianIds,
                List.of()
        );

        final var saved = propertyGateway.save(property);
        return new SyncEntityResult(entity.localId(), saved.getId().id(), SyncEntityStatus.CREATED, null);
    }

    private UUID toUUID(final Object value) {
        if (value == null) return null;
        if (value instanceof UUID u) return u;
        return UUID.fromString(value.toString());
    }

    private BigDecimal toBigDecimal(final Object value) {
        return switch (value) {
            case null -> BigDecimal.ZERO;
            case BigDecimal bd -> bd;
            case Number n -> BigDecimal.valueOf(n.doubleValue());
            default -> new BigDecimal(value.toString());
        };
    }

    private Double toDouble(final Object value) {
        return switch (value) {
            case null -> 0.0;
            case Double d -> d;
            case Number n -> n.doubleValue();
            default -> Double.parseDouble(value.toString());
        };
    }
}
