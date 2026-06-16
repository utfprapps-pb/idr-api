package br.gov.pr.idr.domain.iam.user;

import br.gov.pr.idr.domain.iam.user.query.SearchUserQuery;
import br.gov.pr.idr.domain.iam.user.vo.CPF;
import br.gov.pr.idr.domain.shared.search.Pagination;

import java.util.Optional;

public interface UserGateway {

    User create(final User user);

    boolean existsByCPF(final CPF cpf);

    boolean existsByUsername(final String username);

    boolean existsById(final UserID id);

    Optional<User> findByUsername(final String username);

    Pagination<User> search(final SearchUserQuery query);
}
