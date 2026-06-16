package br.gov.pr.idr.infra.iam.user;

import br.gov.pr.idr.domain.iam.user.User;
import br.gov.pr.idr.domain.iam.user.UserGateway;
import br.gov.pr.idr.domain.iam.user.UserID;
import br.gov.pr.idr.domain.iam.user.query.SearchUserQuery;
import br.gov.pr.idr.domain.iam.user.vo.CPF;
import br.gov.pr.idr.domain.shared.search.Pagination;
import br.gov.pr.idr.infra.iam.user.persistence.UserJPAEntity;
import br.gov.pr.idr.infra.iam.user.persistence.UserJPARepository;
import br.gov.pr.idr.infra.shared.support.PageRequestFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class UserPostgresGateway implements UserGateway {

    private final UserJPARepository repository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User create(User user) {
        final var encodePassword = passwordEncoder.encode(user.getPassword().pasword());
        final var userJpa = UserJPAEntity.from(user, encodePassword);
        return repository.save(userJpa).toDomain();
    }

    @Override
    public boolean existsByCPF(final CPF cpf) {
        return repository.existsByCpf(cpf.value());
    }

    @Override
    public boolean existsByUsername(String username) {
        return repository.existsByUsername(username);
    }

    @Override
    public boolean existsById(final UserID id) {
        return repository.existsById(id.id());
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username).map(UserJPAEntity::toDomain);
    }

    @Override
    public Pagination<User> search(SearchUserQuery query) {
        final var terms = query.query().terms() != null ? query.query().terms().trim() : "";
        final var pageRequest = PageRequestFactory.from(query);
        final var page = repository.search(terms, query.active(), pageRequest);
        return new Pagination<>(page.getNumber(), page.getSize(), page.getTotalElements(),
                page.getContent().stream().map(UserJPAEntity::toDomain).toList());
    }
}
