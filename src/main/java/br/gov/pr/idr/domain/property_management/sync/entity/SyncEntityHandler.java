package br.gov.pr.idr.domain.property_management.sync.entity;

import br.gov.pr.idr.domain.property_management.sync.context.SyncContext;
import br.gov.pr.idr.domain.property_management.sync.vo.OfflineEntityType;

import java.util.Set;

public interface SyncEntityHandler {

    OfflineEntityType type();

    default Set<OfflineEntityType> dependencies() {
        return Set.of();
    }

    SyncEntityResult handle(final OfflineEntityCommand command, final SyncContext context);
}
