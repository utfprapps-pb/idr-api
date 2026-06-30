package br.gov.pr.idr.infra.property_management.sync;

import br.gov.pr.idr.domain.iam.user.User;
import br.gov.pr.idr.domain.iam.user.UserID;
import br.gov.pr.idr.domain.property_management.sync.SyncDownloadGateway;
import br.gov.pr.idr.domain.property_management.sync.SyncSnapshot;
import br.gov.pr.idr.domain.shared.exceptions.NotFoundException;
import br.gov.pr.idr.infra.iam.user.persistence.UserJPARepository;
import br.gov.pr.idr.infra.property_management.city.persistence.CityJPAEntity;
import br.gov.pr.idr.infra.property_management.city.persistence.CityJPARepository;
import br.gov.pr.idr.infra.property_management.producer.persistence.ProducerJPAEntity;
import br.gov.pr.idr.infra.property_management.producer.persistence.ProducerJPARepository;
import br.gov.pr.idr.infra.property_management.region.persistence.RegionJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SyncDownloadPostgresGateway implements SyncDownloadGateway {

    private final UserJPARepository userRepository;
    private final CityJPARepository cityRepository;
    private final RegionJPARepository regionRepository;
    private final ProducerJPARepository producerRepository;

    @Override
    public SyncSnapshot findByTechnicianId(final UserID technicianId) {
        final var user = userRepository.findById(technicianId.id())
                .orElseThrow(() -> NotFoundException.with(User.class, technicianId));

        final Set<java.util.UUID> regionIds = user.getUserPermissions().stream()
                .flatMap(p -> p.getRegionIds().stream())
                .collect(Collectors.toSet());

        if (regionIds.isEmpty()) {
            return new SyncSnapshot(List.of(), List.of(), List.of());
        }

        final var regions = regionRepository.findAllByIdIn(regionIds);
        final var cities = cityRepository.findAllByRegionIdIn(regionIds);
        final Set<java.util.UUID> cityIds = cities.stream()
                .map(CityJPAEntity::getId)
                .collect(Collectors.toSet());
        final List<ProducerJPAEntity> producers =
                cityIds.isEmpty() ? List.of() : producerRepository.findAllByCityIds(cityIds);

        return new SyncSnapshot(
                regions.stream()
                        .map(r -> new SyncSnapshot.RegionInfo(r.getId(), r.getDescription()))
                        .toList(),
                cities.stream()
                        .map(c -> new SyncSnapshot.CityInfo(c.getId(), c.getName(), c.getState()))
                        .toList(),
                producers.stream()
                        .map(p -> new SyncSnapshot.ProducerInfo(
                                p.getId(), p.getName(), p.getCpf(), p.getVersion(), p.getUpdatedAt()))
                        .toList()
        );
    }
}
