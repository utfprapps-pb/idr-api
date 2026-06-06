package br.gov.pr.idr.infra.iam.refresh_token.persistence;

import br.gov.pr.idr.domain.iam.refresh_token.RefreshToken;
import br.gov.pr.idr.domain.iam.refresh_token.RefreshTokenID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity(name = "RefreshToken")
@Table(name = "refresh_token")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenJPAEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private Instant expiresAt;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private boolean revoked;

    public static RefreshTokenJPAEntity from(final RefreshToken domain) {
        return new RefreshTokenJPAEntity(
                domain.getId().id(),
                domain.getToken(),
                domain.getUserId(),
                domain.getUsername(),
                domain.getExpiresAt(),
                domain.getCreatedAt(),
                domain.isRevoked()
        );
    }

    public RefreshToken toDomain() {
        return RefreshToken.with(
                RefreshTokenID.from(this.id),
                this.token,
                this.userId,
                this.username,
                this.expiresAt,
                this.createdAt,
                this.revoked
        );
    }
}
