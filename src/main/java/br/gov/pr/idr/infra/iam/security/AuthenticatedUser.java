package br.gov.pr.idr.infra.iam.security;

import br.gov.pr.idr.application.property_management.property.PropertyAccessPolicy;
import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;
import br.gov.pr.idr.infra.iam.user.persistence.UserJPAEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.function.Supplier;

/**
 * Principal das requisições autenticadas por JWT: o username e o escopo de acesso às propriedades, calculado a partir
 * do usuário que o filtro já carregou (sem nova consulta). O escopo só é calculado se algum endpoint pedir.
 * {@link #toString()} devolve o username, para quem só precisa dele.
 */
public record AuthenticatedUser(String username, Supplier<PropertySearchScope> scope) {

    public static AuthenticatedUser from(final UserDetails userDetails) {
        return new AuthenticatedUser(userDetails.getUsername(),
                () -> PropertyAccessPolicy.resolve(((UserJPAEntity) userDetails).toDomain()));
    }

    @Override
    public String toString() {
        return username;
    }
}
