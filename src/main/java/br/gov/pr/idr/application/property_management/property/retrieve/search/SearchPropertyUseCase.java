package br.gov.pr.idr.application.property_management.property.retrieve.search;

import br.gov.pr.idr.application.shared.stereotype.QueryUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.iam.user.UserGateway;
import br.gov.pr.idr.domain.iam.user.exceptions.UserException;
import br.gov.pr.idr.domain.property_management.property.PropertyGateway;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;

@QueryUseCase
public class SearchPropertyUseCase extends UseCase<SearchPropertyCommand, Pagination<SearchPropertyOutput>> {

    private final PropertyGateway propertyGateway;
    private final UserGateway userGateway;

    public SearchPropertyUseCase(final PropertyGateway propertyGateway, final UserGateway userGateway) {
        this.propertyGateway = propertyGateway;
        this.userGateway = userGateway;
    }

    @Override
    public Pagination<SearchPropertyOutput> execute(final SearchPropertyCommand command) {
        final var user = userGateway.findByUsername(command.username())
                .orElseThrow(() -> new UserException(
                        "Usuário com o username %s não encontrado".formatted(command.username())));

        final var scope = PropertyAccessPolicy.resolve(user);

        return propertyGateway.search(command.query(), scope).map(SearchPropertyOutput::from);
    }
}
