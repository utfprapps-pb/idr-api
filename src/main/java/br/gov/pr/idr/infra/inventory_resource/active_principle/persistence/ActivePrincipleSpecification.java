package br.gov.pr.idr.infra.inventory_resource.active_principle.persistence;

import org.springframework.data.jpa.domain.Specification;

public class ActivePrincipleSpecification {

    private ActivePrincipleSpecification() {}

    public static Specification<ActivePrincipleJPAEntity> withTerms(final String terms) {
        return (root, query, cb) -> {
            if (terms == null || terms.isBlank()) return cb.conjunction();
            final var pattern = "%" + terms.toLowerCase() + "%";
            return cb.like(cb.lower(root.get("name")), pattern);
        };
    }
}
