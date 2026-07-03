package br.gov.pr.idr.application.property_management.sync.download;

import br.gov.pr.idr.application.shared.stereotype.QueryUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.iam.user.UserID;
import br.gov.pr.idr.domain.property_management.sync.SyncDownloadGateway;

@QueryUseCase
public class DownloadSyncUseCase extends UseCase<UserID, DownloadSyncOutput> {

    private final SyncDownloadGateway syncDownloadGateway;

    public DownloadSyncUseCase(final SyncDownloadGateway syncDownloadGateway) {
        this.syncDownloadGateway = syncDownloadGateway;
    }

    @Override
    public DownloadSyncOutput execute(final UserID technicianId) {
        return DownloadSyncOutput.from(syncDownloadGateway.findByTechnicianId(technicianId));
    }
}
