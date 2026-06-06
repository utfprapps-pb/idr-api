package br.gov.pr.idr.domain.iam.refresh_token;

import br.gov.pr.idr.domain.shared.Entity;
import br.gov.pr.idr.domain.shared.validation.DomainError;
import br.gov.pr.idr.domain.shared.validation.ValidationHandler;

import java.time.Instant;
import java.util.UUID;

public class RefreshToken extends Entity<RefreshTokenID> {

    private final String token;
    private final UUID userId;
    private final String username;
    private final Instant expiresAt;
    private final Instant createdAt;
    private boolean revoked;

    RefreshToken(
            final RefreshTokenID id,
            final String token,
            final UUID userId,
            final String username,
            final Instant expiresAt,
            final Instant createdAt,
            final boolean revoked
    ) {
        super(id);
        this.token = token;
        this.userId = userId;
        this.username = username;
        this.expiresAt = expiresAt;
        this.createdAt = createdAt;
        this.revoked = revoked;
        super.selfValidate();
    }

    public static RefreshToken create(final UUID userId, final String username, final long expirationDays) {
        return new RefreshToken(
                RefreshTokenID.unique(),
                UUID.randomUUID().toString(),
                userId,
                username,
                Instant.now().plusSeconds(expirationDays * 24 * 60 * 60),
                Instant.now(),
                false
        );
    }

    public static RefreshToken with(
            final RefreshTokenID id,
            final String token,
            final UUID userId,
            final String username,
            final Instant expiresAt,
            final Instant createdAt,
            final boolean revoked
    ) {
        return new RefreshToken(id, token, userId, username, expiresAt, createdAt, revoked);
    }

    public void revoke() {
        this.revoked = true;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(this.expiresAt);
    }

    @Override
    public void validate(final ValidationHandler handler) {
        if (this.token == null || this.token.isBlank()) {
            handler.append(DomainError.from("token", "Token não pode ser nulo ou vazio."));
        }
        if (this.userId == null) {
            handler.append(DomainError.from("userId", "UserId não pode ser nulo."));
        }
        if (this.username == null || this.username.isBlank()) {
            handler.append(DomainError.from("username", "Username não pode ser nulo ou vazio."));
        }
        if (this.expiresAt == null) {
            handler.append(DomainError.from("expiresAt", "Data de expiração não pode ser nula."));
        }
    }

    public String getToken() { return token; }
    public UUID getUserId() { return userId; }
    public String getUsername() { return username; }
    public Instant getExpiresAt() { return expiresAt; }
    public Instant getCreatedAt() { return createdAt; }
    public boolean isRevoked() { return revoked; }
}
