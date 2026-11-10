package br.gov.pr.idr.infra.inventory_resource.active_principle.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface ActivePrincipleJPARepository extends JpaRepository<ActivePrincipleJPAEntity, UUID>,
        JpaSpecificationExecutor<ActivePrincipleJPAEntity> {
}
