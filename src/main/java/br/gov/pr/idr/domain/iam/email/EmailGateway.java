package br.gov.pr.idr.domain.iam.email;

import java.util.Optional;

public interface EmailGateway {

    Email save(Email email);

    void deleteByEmail(String email);

   Optional<Email> findByEmailAndCode(String email, String code);
}
