package br.gov.pr.idr.application.livestock.breed.retrieve.list;

import br.gov.pr.idr.application.shared.stereotype.QueryUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.livestock.breed.BreedGateway;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;

@QueryUseCase
public class ListBreedUseCase extends UseCase<SearchQuery, Pagination<ListBreedOutput>> {

    private final BreedGateway breedGateway;

    public ListBreedUseCase(BreedGateway breedGateway) {
        this.breedGateway = breedGateway;
    }

    @Override
    public Pagination<ListBreedOutput> execute(final SearchQuery query) {
        return breedGateway.search(query).map(ListBreedOutput::from);
    }
}
