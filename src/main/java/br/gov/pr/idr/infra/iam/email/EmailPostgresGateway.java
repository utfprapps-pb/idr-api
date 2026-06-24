package br.gov.pr.idr.infra.iam.email;

import br.gov.pr.idr.domain.iam.email.Email;
import br.gov.pr.idr.domain.iam.email.EmailGateway;
import br.gov.pr.idr.infra.iam.email.persistence.EmailJPAEntity;
import br.gov.pr.idr.infra.iam.email.persistence.EmailJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EmailPostgresGateway implements EmailGateway {

    private final EmailJPARepository repository;

    @Override
    public Email save(final Email email) {
        return repository.save(EmailJPAEntity.from(email)).toDomain();
    }

    @Override
    public void deleteByEmail(final String email) {
        repository.deleteByRecoveryEmail(email);
    }

    @Override
    public Optional<Email> findByEmailAndCode(final String email, final String code) {
        return repository.findByRecoveryEmailAndRecoveryCode(email, code)
                .map(EmailJPAEntity::toDomain);
    }
}
