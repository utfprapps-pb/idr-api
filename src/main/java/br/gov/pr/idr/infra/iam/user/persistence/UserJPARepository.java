package br.gov.pr.idr.infra.iam.user.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJPARepository extends JpaRepository<UserJPAEntity, Long> {

    boolean existsByCpf(String cpf);

    boolean existsByUsername(String username);
}
