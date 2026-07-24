package br.gov.pr.idr.domain.property_management.sync.snapshot;

import br.gov.pr.idr.domain.property_management.sync.scope.SyncScope;
import br.gov.pr.idr.domain.property_management.sync.entity.SyncItem;

import java.util.List;

public interface SyncSnapshotContributor {

    String collectionName();

    List<SyncItem> contribute(final SyncScope scope);
}
