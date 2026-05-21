package br.gov.pr.idr.domain.iam.user;

import br.gov.pr.idr.domain.iam.user.vo.CPF;

public interface UserGateway {

    User create(final User user);

    boolean existsByCPF(final CPF cpf);

    boolean existsByUsername(final String username);
}
