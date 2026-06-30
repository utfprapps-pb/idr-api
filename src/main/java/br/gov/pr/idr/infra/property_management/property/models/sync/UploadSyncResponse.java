package br.gov.pr.idr.infra.property_management.property.models.sync;

import br.gov.pr.idr.domain.property_management.sync.SyncEntityResult;

import java.util.List;

public record UploadSyncResponse(List<SyncEntityResultResponse> results) {

    public static UploadSyncResponse from(final List<SyncEntityResult> results) {
        return new UploadSyncResponse(
                results.stream().map(SyncEntityResultResponse::from).toList()
        );
    }
}
