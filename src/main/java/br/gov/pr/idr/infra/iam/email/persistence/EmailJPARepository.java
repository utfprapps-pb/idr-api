package br.gov.pr.idr.infra.iam.email.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EmailJPARepository extends JpaRepository<EmailJPAEntity, UUID> {

    Optional<EmailJPAEntity> findByRecoveryEmailAndRecoveryCode(String recoveryEmail, String recoveryCode);

    void deleteByRecoveryEmail(String recoveryEmail);
}
