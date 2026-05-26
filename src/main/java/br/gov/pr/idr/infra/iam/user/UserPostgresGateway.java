package br.gov.pr.idr.infra.iam.user;

import br.gov.pr.idr.domain.iam.user.User;
import br.gov.pr.idr.domain.iam.user.UserGateway;
import br.gov.pr.idr.domain.iam.user.UserID;
import br.gov.pr.idr.domain.iam.user.vo.CPF;
import br.gov.pr.idr.domain.property_management.city.CityID;
import br.gov.pr.idr.infra.iam.user.persistence.UserJPAEntity;
import br.gov.pr.idr.infra.iam.user.persistence.UserJPARepository;
import br.gov.pr.idr.infra.property_management.city.persistence.CityJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class UserPostgresGateway implements UserGateway {

    private final UserJPARepository repository;
    private final CityJPARepository cityRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User create(User user) {
        final var encodePassword = passwordEncoder.encode(user.getPassword().pasword());
        final var city = Optional.ofNullable(user.getCity())
                .map(CityID::id)
                .flatMap(cityRepository::findById)
                .orElse(null);
        final var userJpa = UserJPAEntity.from(user, city, encodePassword);
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
}
