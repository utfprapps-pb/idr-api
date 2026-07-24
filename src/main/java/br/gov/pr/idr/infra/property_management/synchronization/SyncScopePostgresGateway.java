package br.gov.pr.idr.infra.property_management.synchronization;

import br.gov.pr.idr.domain.iam.user.User;
import br.gov.pr.idr.domain.iam.user.UserID;
import br.gov.pr.idr.domain.property_management.city.CityID;
import br.gov.pr.idr.domain.property_management.region.RegionID;
import br.gov.pr.idr.domain.property_management.sync.scope.SyncScopeGateway;
import br.gov.pr.idr.domain.property_management.sync.technician.TechnicianScope;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;
import br.gov.pr.idr.infra.iam.user.persistence.UserJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SyncScopePostgresGateway implements SyncScopeGateway {

    private final UserJPARepository userRepository;

    @Override
    public TechnicianScope resolveByTechnician(final UserID technicianId) {
        final var user = userRepository.findById(technicianId.id())
                .orElseThrow(() -> NotFoundException.with(User.class, technicianId));

        final Set<RegionID> regionIds = user.getUserPermissions().stream()
                .flatMap(p -> p.getRegionIds().stream())
                .map(RegionID::from)
                .collect(Collectors.toSet());

        final Set<CityID> cityIds = user.getUserPermissions().stream()
                .flatMap(p -> p.getCityIds().stream())
                .map(CityID::from)
                .collect(Collectors.toSet());

        return new TechnicianScope(regionIds, cityIds);
    }
}
