package br.gov.pr.idr.infra.property_management.region;

import br.gov.pr.idr.domain.property_management.region.Region;
import br.gov.pr.idr.domain.property_management.region.RegionGateway;
import br.gov.pr.idr.domain.property_management.region.RegionID;
import br.gov.pr.idr.infra.property_management.region.persistence.RegionJPAEntity;
import br.gov.pr.idr.infra.property_management.region.persistence.RegionJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class RegionPostgresGateway implements RegionGateway {

    private final RegionJPARepository repository;

    @Override
    public boolean existsById(RegionID id) {
        return repository.existsById(id.id());
    }

    @Override
    public Region save(final Region region) {
        return repository.save(RegionJPAEntity.from(region))
                .toAggregate();
    }

    @Override
    public boolean existsByDescription(final String description) {
        return repository.existsByDescriptionIgnoreCase(description);
    }

    @Override
    public Optional<Region> findByID(final RegionID id) {
        return repository.findById(id.id()).map(RegionJPAEntity::toAggregate);
    }

    @Override
    public Region update(final Region region) {
        return this.save(region);
    }
}
