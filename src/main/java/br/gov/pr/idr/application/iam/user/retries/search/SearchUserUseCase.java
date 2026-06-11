package br.gov.pr.idr.application.iam.user.retries.search;

import br.gov.pr.idr.application.shared.QueryUseCase;
import br.gov.pr.idr.application.shared.UseCase;
import br.gov.pr.idr.domain.iam.user.UserGateway;
import br.gov.pr.idr.domain.shared.search.Pagination;
import br.gov.pr.idr.domain.shared.search.SearchQuery;

@QueryUseCase
public class SearchUserUseCase extends UseCase<SearchQuery, Pagination<SearchUserOutput>> {

    private final UserGateway userGateway;

    public SearchUserUseCase(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    @Override
    public Pagination<SearchUserOutput> execute(SearchQuery query) {
        return userGateway.search(query).map(SearchUserOutput::from);
    }
}
