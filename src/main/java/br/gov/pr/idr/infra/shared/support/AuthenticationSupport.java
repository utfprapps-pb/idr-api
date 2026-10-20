package br.gov.pr.idr.infra.shared.support;

import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;
import br.gov.pr.idr.infra.iam.security.AuthenticatedUser;
import org.springframework.security.core.Authentication;

public final class AuthenticationSupport {

    private AuthenticationSupport() {}

    public static String username(final Authentication authentication) {
        final var principal = authentication == null ? null : authentication.getPrincipal();
        if (principal == null) {
            throw new IllegalStateException("Authentication principal is null");
        }
        return principal.toString();
    }

    /** Escopo de acesso às propriedades do usuário autenticado, já resolvido pelo filtro JWT. */
    public static PropertySearchScope scope(final Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof AuthenticatedUser user)) {
            throw new IllegalStateException("Authentication principal is not an AuthenticatedUser");
        }
        return user.scope();
    }
}
