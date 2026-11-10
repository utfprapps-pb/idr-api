package br.gov.pr.idr.domain.inventory_resource.active_principle;

import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;

import java.util.Optional;

public interface ActivePrincipleGateway {

    boolean existsById(final ActivePrincipleID id);

    ActivePrinciple save(final ActivePrinciple principle);

    Optional<ActivePrinciple> findById(final ActivePrincipleID id);

    ActivePrinciple update(final ActivePrinciple principle);

    Pagination<ActivePrinciple> search(final SearchQuery query);

    void deleteById(final ActivePrincipleID id);
}
