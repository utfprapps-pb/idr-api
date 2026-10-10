package br.gov.pr.idr.infra.shared.persistence;

import org.springframework.data.jpa.domain.Specification;

public final class LikeSpecification {

    private LikeSpecification() {}

    public static <T> Specification<T> withTerms(final String terms, final String field) {
        return (root, query, cb) -> {
            if (terms == null || terms.isBlank()) return cb.conjunction();
            final var pattern = "%" + terms.toLowerCase() + "%";
            return cb.like(cb.lower(root.get(field)), pattern);
        };
    }
}
