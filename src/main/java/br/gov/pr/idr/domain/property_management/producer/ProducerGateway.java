package br.gov.pr.idr.domain.property_management.producer;

import br.gov.pr.idr.domain.iam.user.vo.CPF;
import br.gov.pr.idr.domain.shared.search.Pagination;
import br.gov.pr.idr.domain.shared.search.SearchQuery;

import java.util.Optional;

public interface ProducerGateway {

    Producer save(final Producer producer);

    Producer update(final Producer producer);

    Optional<Producer> findById(final ProducerID id);

    boolean existsById(final ProducerID id);

    boolean existsByCpf(final CPF cpf);

    Optional<Producer> findByCpf(final CPF cpf);

    Pagination<Producer> search(final SearchQuery query);
}
