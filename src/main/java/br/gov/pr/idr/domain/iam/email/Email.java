package br.gov.pr.idr.domain.iam.email;

import br.gov.pr.idr.domain.shared.AggregateRoot;
import br.gov.pr.idr.domain.shared.validation.DomainError;
import br.gov.pr.idr.domain.shared.validation.ValidationHandler;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class Email extends AggregateRoot<EmailID> {

    private final String code;
    private final String email;
    private final String userName;
    private final Instant createdAt;
    private final Instant expiresAt;

    Email(
            final EmailID id,
            final String code,
            final String email,
            final String userName,
            final Instant createdAt,
            final Instant expiresAt
    ) {
        super(id);
        this.code = code;
        this.email = email;
        this.userName = userName;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        super.selfValidate();
    }

    public static Email create(final String email, final String userName) {
        final var code = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        final var now = Instant.now();
        return new Email(
                EmailID.unique(),
                code,
                email,
                userName,
                now,
                now.plus(5, ChronoUnit.MINUTES)
        );
    }

    public static Email with(
            final EmailID id,
            final String code,
            final String email,
            final String userName,
            final Instant createdAt,
            final Instant expiresAt
    ) {
        return new Email(id, code, email, userName, createdAt, expiresAt);
    }

    public boolean isExpired() {
        return Instant.now().isAfter(this.expiresAt);
    }

    @Override
    public void validate(final ValidationHandler handler) {
        if (email == null || email.isBlank()) {
            handler.append(DomainError.from("email", "E-mail não pode ser nulo"));
        }
        if (code == null || code.isBlank()) {
            handler.append(DomainError.from("code", "Código não pode ser nulo"));
        }
    }

    public String getCode() {
        return code;
    }

    public String getEmail() {
        return email;
    }

    public String getUserName() {
        return userName;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }
}
