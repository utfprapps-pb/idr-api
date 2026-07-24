package br.gov.pr.idr.infra.property_management.synchronization.persistence;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class SyncIdMappingId implements Serializable {

    private UUID technicianId;
    private UUID localId;
}
