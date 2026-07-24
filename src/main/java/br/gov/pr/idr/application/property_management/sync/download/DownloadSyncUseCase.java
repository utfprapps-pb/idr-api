package br.gov.pr.idr.application.property_management.sync.download;

import br.gov.pr.idr.domain.property_management.sync.scope.SyncScope;
import br.gov.pr.idr.domain.property_management.sync.query.DownloadSyncQuery;
import br.gov.pr.idr.domain.property_management.sync.snapshot.SyncSnapshotContributor;
import br.gov.pr.idr.application.shared.stereotype.QueryUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.property_management.sync.entity.SyncItem;
import br.gov.pr.idr.domain.property_management.sync.scope.SyncScopeGateway;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@QueryUseCase
public class DownloadSyncUseCase extends UseCase<DownloadSyncQuery, DownloadSyncOutput> {

    private static final String SCHEMA_VERSION = "1.0";

    private final List<SyncSnapshotContributor> contributors;
    private final SyncScopeGateway scopeGateway;

    public DownloadSyncUseCase(final List<SyncSnapshotContributor> contributors,
                               final SyncScopeGateway scopeGateway) {
        this.contributors = contributors;
        this.scopeGateway = scopeGateway;
    }

    @Override
    public DownloadSyncOutput execute(final DownloadSyncQuery query) {
        final var serverTimestamp = Instant.now();
        final var technicianScope = scopeGateway.resolveByTechnician(query.technicianId());
        final var scope = new SyncScope(
                query.technicianId(),
                technicianScope.regionIds(),
                technicianScope.cityIds(),
                query.since());

        final Map<String, List<SyncItem>> collections = new LinkedHashMap<>();
        for (final var contributor : contributors) {
            collections.put(contributor.collectionName(), contributor.contribute(scope));
        }

        return new DownloadSyncOutput(SCHEMA_VERSION, serverTimestamp, collections);
    }
}
