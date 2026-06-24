package br.gov.pr.idr.infra.iam.email.persistence;

import br.gov.pr.idr.domain.iam.email.Email;
import br.gov.pr.idr.domain.iam.email.EmailID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity(name = "Email")
@Table(name = "password_recovery")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailJPAEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false)
    private String recoveryCode;

    @Column(nullable = false)
    private String recoveryEmail;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant expiresAt;

    public static EmailJPAEntity from(final Email email) {
        return new EmailJPAEntity(
                email.getId().id(),
                email.getCode(),
                email.getEmail(),
                email.getUserName(),
                email.getCreatedAt(),
                email.getExpiresAt()
        );
    }

    public Email toDomain() {
        return Email.with(
                EmailID.from(this.id),
                this.recoveryCode,
                this.recoveryEmail,
                this.userName,
                this.createdAt,
                this.expiresAt
        );
    }
}
