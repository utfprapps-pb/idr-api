package br.gov.pr.idr.domain.iam.refresh_token.exceptions;

import br.gov.pr.idr.domain.shared.tactical.exceptions.DomainException;

import java.util.List;

/** Refresh token inexistente, revogado ou expirado: a sessão não pode ser renovada e o usuário precisa logar. */
public final class InvalidRefreshTokenException extends DomainException {

    private InvalidRefreshTokenException(final String message) {
        super(message, List.of());
    }

    public static InvalidRefreshTokenException with(final String message) {
        return new InvalidRefreshTokenException(message);
    }
}
