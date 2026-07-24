package br.gov.pr.idr.application.property_management.sync.upload;

import br.gov.pr.idr.domain.iam.user.UserID;

import java.util.List;

public record UploadSyncCommand(UserID technicianId, List<OfflineEntityCommand> entities) {


}
