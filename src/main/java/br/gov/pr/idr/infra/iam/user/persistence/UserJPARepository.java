package br.gov.pr.idr.infra.iam.user.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserJPARepository extends JpaRepository<UserJPAEntity, UUID> {

    boolean existsByCpf(String cpf);

    boolean existsByUsername(String username);

    Optional<UserJPAEntity> findByUsername(String username);
}
