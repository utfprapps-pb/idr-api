package br.gov.pr.idr.application.livestock.animalsize.retrieve.list;

import br.gov.pr.idr.application.shared.stereotype.QueryUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.livestock.animalsize.AnimalSizeGateway;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;

@QueryUseCase
public class ListAnimalSizeUseCase extends UseCase<SearchQuery, Pagination<ListAnimalSizeOutput>> {

    private final AnimalSizeGateway animalSizeGateway;

    public ListAnimalSizeUseCase(AnimalSizeGateway animalSizeGateway) {
        this.animalSizeGateway = animalSizeGateway;
    }

    @Override
    public Pagination<ListAnimalSizeOutput> execute(final SearchQuery query) {
        return animalSizeGateway.search(query).map(ListAnimalSizeOutput::from);
    }
}
