package br.gov.pr.idr.infra.property_management.producer;

import br.gov.pr.idr.domain.iam.user.vo.CPF;
import br.gov.pr.idr.domain.property_management.producer.Producer;
import br.gov.pr.idr.domain.property_management.producer.ProducerGateway;
import br.gov.pr.idr.domain.property_management.producer.ProducerID;
import br.gov.pr.idr.domain.shared.search.Pagination;
import br.gov.pr.idr.domain.shared.search.SearchQuery;
import br.gov.pr.idr.infra.property_management.producer.persistence.ProducerJPAEntity;
import br.gov.pr.idr.infra.property_management.producer.persistence.ProducerJPARepository;
import br.gov.pr.idr.infra.shared.support.PageRequestFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProducerPostgresGateway implements ProducerGateway {

    private final ProducerJPARepository repository;

    @Override
    public Producer save(final Producer producer) {
        return repository.save(ProducerJPAEntity.fromDomain(producer)).toDomain();
    }

    @Override
    public Producer update(final Producer producer) {
        return this.save(producer);
    }

    @Override
    public Optional<Producer> findById(final ProducerID id) {
        return repository.findById(id.id()).map(ProducerJPAEntity::toDomain);
    }

    @Override
    public boolean existsById(final ProducerID id) {
        return repository.existsById(id.id());
    }

    @Override
    public boolean existsByCpf(final CPF cpf) {
        return repository.existsByCpf(cpf.value());
    }

    @Override
    public Optional<Producer> findByCpf(final CPF cpf) {
        return repository.findByCpf(cpf.value()).map(ProducerJPAEntity::toDomain);
    }

    @Override
    public Pagination<Producer> search(final SearchQuery query) {
        final var terms = query.terms() != null ? query.terms().trim() : "";
        final var pageRequest = PageRequestFactory.from(query);
        final var page = repository.search(terms, pageRequest);
        return new Pagination<>(page.getNumber(), page.getSize(), page.getTotalElements(),
                page.getContent().stream().map(ProducerJPAEntity::toDomain).toList());
    }
}
