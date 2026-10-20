package br.gov.pr.idr.infra.iam.security;

import br.gov.pr.idr.application.property_management.property.PropertyAccessPolicy;
import br.gov.pr.idr.domain.iam.user.UserID;
import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;
import br.gov.pr.idr.infra.iam.user.persistence.UserJPAEntity;

/**
 * Principal das requisições autenticadas por JWT: o username e o escopo de acesso às propriedades, resolvido uma vez
 * a partir das permissões do usuário que o filtro já carregou (sem nova consulta e sem guardar a entidade).
 * {@link #toString()} devolve o username, para quem só precisa dele.
 */
public record AuthenticatedUser(String username, PropertySearchScope scope) {

    public static AuthenticatedUser from(final UserJPAEntity user) {
        return new AuthenticatedUser(user.getUsername(),
                PropertyAccessPolicy.resolve(UserID.from(user.getId()), user.getDomainPermissions()));
    }

    @Override
    public String toString() {
        return username;
    }
}
