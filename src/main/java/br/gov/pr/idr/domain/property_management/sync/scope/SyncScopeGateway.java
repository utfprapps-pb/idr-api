package br.gov.pr.idr.domain.property_management.sync.scope;

import br.gov.pr.idr.domain.iam.user.UserID;
import br.gov.pr.idr.domain.property_management.sync.technician.TechnicianScope;

public interface SyncScopeGateway {

    TechnicianScope resolveByTechnician(final UserID technicianId);
}
