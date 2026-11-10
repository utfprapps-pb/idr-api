package br.gov.pr.idr.application.inventory_resource.active_principle.retrieve.list;

import br.gov.pr.idr.application.shared.stereotype.QueryUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.inventory_resource.active_principle.ActivePrincipleGateway;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;

@QueryUseCase
public class ListActivePrincipleUseCase extends UseCase<SearchQuery, Pagination<ListActivePrincipleOutput>> {

    private final ActivePrincipleGateway activePrincipleGateway;

    public ListActivePrincipleUseCase(ActivePrincipleGateway activePrincipleGateway) {
        this.activePrincipleGateway = activePrincipleGateway;
    }

    @Override
    public Pagination<ListActivePrincipleOutput> execute(final SearchQuery query) {
        return activePrincipleGateway.search(query).map(ListActivePrincipleOutput::from);
    }
}
