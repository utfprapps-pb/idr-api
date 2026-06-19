package br.gov.pr.idr.application.property_management.producer.retrieve.search;

import br.gov.pr.idr.application.shared.QueryUseCase;
import br.gov.pr.idr.application.shared.UseCase;
import br.gov.pr.idr.domain.property_management.producer.ProducerGateway;
import br.gov.pr.idr.domain.shared.search.Pagination;
import br.gov.pr.idr.domain.shared.search.SearchQuery;

@QueryUseCase
public class SearchProducerUseCase extends UseCase<SearchQuery, Pagination<SearchProducerOutput>> {

    private final ProducerGateway producerGateway;

    public SearchProducerUseCase(final ProducerGateway producerGateway) {
        this.producerGateway = producerGateway;
    }

    @Override
    public Pagination<SearchProducerOutput> execute(final SearchQuery query) {
        return producerGateway.search(query).map(SearchProducerOutput::from);
    }
}
