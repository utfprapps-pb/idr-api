package br.gov.pr.idr.infra.shared.support;

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
}
