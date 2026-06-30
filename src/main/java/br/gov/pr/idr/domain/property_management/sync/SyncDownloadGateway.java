package br.gov.pr.idr.domain.property_management.sync;

import br.gov.pr.idr.domain.iam.user.UserID;

public interface SyncDownloadGateway {

    SyncSnapshot findByTechnicianId(UserID technicianId);
}
