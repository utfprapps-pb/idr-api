package br.gov.pr.idr.infra.property_management.region.models.list;

import br.gov.pr.idr.application.shared.QueryUseCase;
import br.gov.pr.idr.application.shared.UseCase;
import br.gov.pr.idr.domain.property_management.region.RegionGateway;
import br.gov.pr.idr.domain.shared.search.Pagination;
import br.gov.pr.idr.domain.shared.search.SearchQuery;

@QueryUseCase
public class ListRegionUseCase extends UseCase<SearchQuery, Pagination<ListRegionOutput>> {

    private final RegionGateway regionGateway;

    public ListRegionUseCase(RegionGateway regionGateway) {
        this.regionGateway = regionGateway;
    }

    @Override
    public Pagination<ListRegionOutput> execute(SearchQuery command) {
        return regionGateway.search(command)
                            .map(ListRegionOutput::from);
    }
}
