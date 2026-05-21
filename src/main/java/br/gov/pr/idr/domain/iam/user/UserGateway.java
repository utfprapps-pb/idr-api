package br.gov.pr.idr.domain.iam.user;

import br.gov.pr.idr.domain.iam.user.vo.CPF;

import java.util.Optional;

public interface UserGateway {

    User create(final User user);

    boolean existsByCPF(final CPF cpf);

    boolean existsByUsername(final String username);

    Optional<User> findByUsername(final String username);
}
