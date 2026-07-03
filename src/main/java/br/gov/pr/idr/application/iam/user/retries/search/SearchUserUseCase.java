package br.gov.pr.idr.application.iam.user.retries.search;

import br.gov.pr.idr.application.shared.stereotype.QueryUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.iam.user.UserGateway;
import br.gov.pr.idr.domain.iam.user.query.SearchUserQuery;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;

@QueryUseCase
public class SearchUserUseCase extends UseCase<SearchUserQuery, Pagination<SearchUserOutput>> {

    private final UserGateway userGateway;

    public SearchUserUseCase(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    @Override
    public Pagination<SearchUserOutput> execute(SearchUserQuery query) {
        return userGateway.search(query).map(SearchUserOutput::from);
    }
}
