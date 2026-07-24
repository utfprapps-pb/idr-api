package br.gov.pr.idr.infra.iam.user.sync;

import br.gov.pr.idr.domain.property_management.sync.scope.SyncScope;
import br.gov.pr.idr.domain.property_management.sync.snapshot.SyncSnapshotContributor;
import br.gov.pr.idr.domain.iam.user.UserRole;
import br.gov.pr.idr.domain.property_management.sync.entity.SyncItem;
import br.gov.pr.idr.infra.iam.user.persistence.UserJPAEntity;
import br.gov.pr.idr.infra.iam.user.persistence.UserJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TechnicianSyncContributor implements SyncSnapshotContributor {

    private final UserJPARepository userRepository;

    @Override
    public String collectionName() {
        return "technicians";
    }

    @Override
    public List<SyncItem> contribute(final SyncScope scope) {
        if (!scope.isIncremental()) {
            return userRepository.findAllActiveByPermissionRole(UserRole.TECNICO).stream()
                    .map(TechnicianSyncContributor::toActiveItem)
                    .toList();
        }

        final var since = scope.since();
        final List<SyncItem> items = new ArrayList<>();
        userRepository.findActiveByPermissionRoleUpdatedAfter(UserRole.TECNICO, since)
                .forEach(u -> items.add(toActiveItem(u)));
        userRepository.findInactiveByPermissionRoleUpdatedAfter(UserRole.TECNICO, since)
                .forEach(u -> items.add(toTombstone(u)));
        return items;
    }

    private static SyncItem toActiveItem(final UserJPAEntity user) {
        return SyncItem.active(user.getId(), Map.of("name", user.getName()), 0L, user.getUpdatedAt());
    }

    private static SyncItem toTombstone(final UserJPAEntity user) {
        return SyncItem.tombstone(user.getId(), 0L, user.getUpdatedAt());
    }
}
