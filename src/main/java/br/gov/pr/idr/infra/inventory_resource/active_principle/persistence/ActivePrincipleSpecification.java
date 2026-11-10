package br.gov.pr.idr.infra.inventory_resource.active_principle.persistence;

import br.gov.pr.idr.infra.shared.persistence.LikeSpecification;
import org.springframework.data.jpa.domain.Specification;

public class ActivePrincipleSpecification {

    private ActivePrincipleSpecification() {}

    public static Specification<ActivePrincipleJPAEntity> withTerms(final String terms) {
        return LikeSpecification.withTerms(terms, "name");
    }
}
