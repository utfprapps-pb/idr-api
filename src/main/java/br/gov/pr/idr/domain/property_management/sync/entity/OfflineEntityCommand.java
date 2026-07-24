package br.gov.pr.idr.domain.property_management.sync.entity;

import br.gov.pr.idr.domain.property_management.sync.vo.OfflineEntityType;

import java.util.Map;
import java.util.UUID;

public record OfflineEntityCommand(
            OfflineEntityType type,
            UUID localId,
            Map<String, Object> data
    ) {}
